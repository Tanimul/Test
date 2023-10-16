package bd.com.media365.ratehammer_sdk.common.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bd.com.media365.ratehammer_sdk.utils.DataStorePrefUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    application: Application,
    private val dataStorePrefUtils: DataStorePrefUtils
) :
    AndroidViewModel(application) {


    //Get Value to ModelFields Store
    fun getIntValue(key: String): Flow<Int?> {
        return dataStorePrefUtils.getIntValue(key)
    }

    fun getStringValue(key: String): Flow<String?> {
        return dataStorePrefUtils.getStringValue(key)
    }

    fun getFloatValue(key: String): Flow<Float?> {
        return dataStorePrefUtils.getFloatValue(key)
    }

    fun getDoubleValue(key: String): Flow<Double?> {
        return dataStorePrefUtils.getDoubleValue(key)
    }

    fun getLongValue(key: String): Flow<Long?> {
        return dataStorePrefUtils.getLongValue(key)
    }

    fun getBooleanValue(key: String): Flow<Boolean?> {
        return dataStorePrefUtils.getBooleanValue(key)
    }


    fun saveToDataStore(key: String, value: Any) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePrefUtils.saveValue(key, value)
        }
    }

    fun clearDataStore() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePrefUtils.clear()
        }
    }

    fun removeKeyDataStore(key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePrefUtils.removeKey(key)
        }
    }


}