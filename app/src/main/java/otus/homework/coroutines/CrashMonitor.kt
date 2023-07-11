package otus.homework.coroutines

import otus.homework.coroutines.domain.Error
import otus.homework.coroutines.domain.ResultException

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */

    fun trackWarning(result: Any): String {
        val e: String
        when (result) {
            is Error<*> -> {
                e = "Error: Error code: ${result.errorCode}(${result.errorMessage})"
                println(e)
            }

            is ResultException<*> -> {
                e = "Exception: ${result.exceptionMessage}"
                println(e)
            }

            else -> e = UNKNOWN_EXCEPTION
        }

        return e

    }

    private const val UNKNOWN_EXCEPTION = "Unknown Exception"
}
