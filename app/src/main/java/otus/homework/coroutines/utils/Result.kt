package otus.homework.coroutines.utils

sealed class Result<out T>{
    data object Default: Result<Nothing>()
    data object Loading: Result<Nothing>()

    class Success<out T>(val data: T) : Result<T>()
    class Error(val errorMessage: String?) : Result<Nothing>()
}

