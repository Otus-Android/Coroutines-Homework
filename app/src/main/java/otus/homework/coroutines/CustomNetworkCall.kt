package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

object CustomNetworkCall {
    suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher = Dispatchers.IO, apiCall: suspend () -> T): Result<T> {
        return withContext(dispatcher) {
            try {
                Result.Success(apiCall.invoke())
            } catch (throwable: Exception) {
                when (throwable) {
                    is CancellationException -> throw throwable
                    is SocketTimeoutException -> Result.GenericError(SocketTimeoutException("Не удалось получить ответ от сервером"))
                    else -> {
                        Result.GenericError(throwable)
                    }
                }
            }
        }
    }
}