package otus.homework.coroutines.viewModel

import kotlinx.coroutines.CancellationException


sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val e: Throwable) : Result<Nothing>()

    companion object {
        inline fun <T> tryWith(block: () -> T): Result<T> {
            return try {
               Success(block.invoke())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Error(e)
            }
        }
    }
}
