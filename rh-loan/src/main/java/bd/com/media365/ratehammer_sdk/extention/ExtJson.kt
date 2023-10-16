package bd.com.media365.ratehammer_sdk.extention

import com.google.gson.Gson

object ExtJson {

    fun Any.toJson(): String? {
        return Gson().toJson(this)
    }
}