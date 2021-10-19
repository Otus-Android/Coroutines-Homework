package otus.homework.coroutines.data

sealed class CatResult<T> {
    data class Success<T>(val data: T) : CatResult<T>()
    data class Error(val exception: Exception) : CatResult<Nothing>()
}
