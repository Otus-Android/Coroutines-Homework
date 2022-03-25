package otus.homework.coroutines.utils

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import otus.homework.coroutines.BuildConfig

class MockInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if(BuildConfig.DEBUG) {
            val url: String = chain.request().url.toUrl().path
            Log.i("MockInterceptor", url)
            val responseString : JSONObject = when {
                url.contains("/facts/random")    -> getFact()
                url.contains("/meow") -> getImage()
                else -> serverError()
            }
            val body = responseString.toString().toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())
            return chain.proceed(chain.request())
                .newBuilder()
                .code(200)
                .protocol(Protocol.HTTP_1_1)
                .message(responseString.toString())
                .body(body)
                .addHeader("content-type", "application/json")
                .build()

        } else {
            throw IllegalAccessException("use for debug mode")
        }
    }

    private fun getFact(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("createdAt", "2011-01-26T19:01:12Z")
        jsonObject.put("deleted", false)
        jsonObject.put("_id", 1296269)
        jsonObject.put("text", "The British Shorthair is the pedigreed version of the traditional British domestic cat")
        jsonObject.put("source", "sourceValue")
        jsonObject.put("used", true)
        jsonObject.put("type", "typeValue")
        jsonObject.put("user", "userValue")
        jsonObject.put("updatedAt", "2011-01-26T19:01:12Z")
        return jsonObject
    }

    private fun getImage(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("file", "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c0/A_British_Shorthair_cat.jpg/641px-A_British_Shorthair_cat.jpg")
        return jsonObject
    }

    private fun serverError(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("data", "the server is not responsible")
        return jsonObject
    }
}