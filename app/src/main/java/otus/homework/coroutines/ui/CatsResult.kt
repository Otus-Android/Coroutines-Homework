package otus.homework.coroutines.ui

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val error: String) : Result<T>()
    data class Loading<T>(val isLoading: Boolean) : Result<T>()
}