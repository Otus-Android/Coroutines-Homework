package otus.homework.coroutines

import otus.homework.coroutines.domain.Error
import otus.homework.coroutines.domain.ResultException
import java.net.SocketTimeoutException

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
                e = if (result.throwable is SocketTimeoutException) SOCKET_TIMEOUT_EXCEPTION_MESSAGE
                else "Exception: ${result.throwable.message}"
                println(e)
            }

            else -> e = UNKNOWN_EXCEPTION
        }

        return e

    }

    private const val SOCKET_TIMEOUT_EXCEPTION_MESSAGE = "Не удалось получить ответ от сервера"

    private const val UNKNOWN_EXCEPTION = "Unknown Exception"
}
