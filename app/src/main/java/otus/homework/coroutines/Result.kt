package otus.homework.coroutines

import otus.homework.coroutines.presentation.ErrorType

sealed interface Result<out T> {
    class Success<T>(val data: T) : Result<T>
    class Error(val type: ErrorType) : Result<Nothing>
}