package otus.homework.coroutines.presentation

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    object Loading : Result<Nothing>()
    data class Error(val error: CatsError) : Result<Nothing>()
}