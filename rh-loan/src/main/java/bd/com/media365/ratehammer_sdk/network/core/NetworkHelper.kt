package bd.com.media365.ratehammer_sdk.network.core

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import bd.com.media365.ratehammer_sdk.extention.ExtJson.toJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException


@Keep
suspend fun <T> performNetworkCallWithRetries(
    call: suspend () -> Response<T>,
    maxRetries: Int = 2
): Response<T> {
    var retryCount = 0
    while (retryCount < maxRetries) {
        try {
            return call.invoke()
        } catch (e: IOException) {
            if (retryCount == maxRetries - 1) {
                throw e
            } else {
                Log.d("Retrying", "performNetworkCallWithRetries: ")
                delay(500) // Wait for 0.5 seconds before retrying
            }
        }
        retryCount++
    }
    throw IllegalStateException("Maximum number of retries exceeded")
}


@Keep
fun <T : Any> networkCall(
    connectivityManager: ConnectivityManager,
    call: suspend () -> Response<T>
): Flow<Resource<T>> = flow {

    Log.d("NetworkCall", "Before Loading")
    emit(Resource.Loading())

    if (!connectivityManager.isInternetAvailable()) {
        Log.d("NetworkCall", "No Internet")
        emit(Resource.NetworkError())
        return@flow
    } else {
        val response = performNetworkCallWithRetries(call)
        if (response.isSuccessful) {
            var message: String? = null
            try {
                val body = JSONObject(response.body()?.toJson().toString())
                if (body.has("message"))
                    message = body.getString("message")
            } catch (e: Exception) {
                Log.d("NetworkCall", "messageParse: ${e.localizedMessage}")
            }
            Log.d("NetworkCall", "${response.body()?.toJson()}")
            emit(Resource.Success(response.body(), message))
            return@flow
        } else {
            val jsonObject = JSONObject(response.errorBody()?.string())
            val errorBody =
                if (jsonObject.has("errors")) jsonObject.get("errors").toString() else ""
            val errorMessage =
                if (jsonObject.has("message")) jsonObject.get("message").toString() else ""

            val errorCode = response.code()
            val error = ErrorBody(errorMessage, errorCode, errorBody)

            emit(Resource.Error(error))

            return@flow
        }
    }
}.catch { e ->
    Log.d("NetworkRequest", "networkCall: ${e.localizedMessage}")
    for (i in e.stackTrace) {
        Log.d("NetworkRequest", "$i")
    }
    emit(Resource.Error(ErrorBody(e.localizedMessage, null, null)))
}

fun ConnectivityManager.isInternetAvailable(): Boolean {
    val result: Boolean
    val networkCapabilities = activeNetwork ?: return false
    val actNw = getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }

    return result
}

inline fun <reified T> MutableLiveData<Resource<T>>.collect(network: Flow<Resource<T>>) {
    CoroutineScope(Dispatchers.IO).launch {
        network.collect {
            postValue(it)
        }
    }
}



