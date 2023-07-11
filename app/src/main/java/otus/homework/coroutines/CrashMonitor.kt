package otus.homework.coroutines

import android.util.Log
import otus.homework.coroutines.domain.Error
import otus.homework.coroutines.domain.ResultException

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */

    fun trackWarning(result: Any): String {
        val e:String
        when(result) {
            is Error<*> -> {
                e = "Error: Error code: ${result.errorCode}(${result.errorMessage})"
                Log.d("CrashMonitor", e)}
            is ResultException<*> ->{
                e = "Exception: ${result.exceptionMessage}"
                Log.d("CrashMonitor", e)}
            else -> e = UNKNOWN_EXCEPTION
        }

        return e

    }

    private const val UNKNOWN_EXCEPTION = "Unknown Exception"
}