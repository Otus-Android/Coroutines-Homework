package otus.homework.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import otus.homework.coroutines.utils.CrashMonitor
import java.lang.Exception
import java.net.SocketTimeoutException

interface ExceptionWrapper {

    fun exceptionHandler(
        callback: (Throwable) -> Unit
    ): CoroutineExceptionHandler

    suspend fun apiCall(
        onSuccess: suspend () -> Unit,
        onError: (String?) -> Unit
    )
}

class ExceptionWrapperImpl : ExceptionWrapper {

    override fun exceptionHandler(callback: (Throwable) -> Unit) =
        CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning(throwable)
            callback.invoke(throwable)
        }

    override suspend fun apiCall(
        onSuccess: suspend () -> Unit,
        onError: (String?) -> Unit
    ) {
        try {
            onSuccess.invoke()
        } catch (error: SocketTimeoutException) {
            onError.invoke("Не удалось получить ответ от сервера")
        } catch (error: Exception) {
            CrashMonitor.trackWarning(error)
            onError.invoke(error.message)
        }
    }
}