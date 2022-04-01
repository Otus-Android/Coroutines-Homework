package otus.homework.coroutines

import java.lang.Exception

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val error: Exception) : Result<T>()

    fun getSuccessData(): T? {
        return when (this) {
            is Result.Success -> data
            else -> null
        }
    }
}