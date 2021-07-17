package otus.homework.coroutines

sealed class Result<out R> {
    data class Success<out R>(val data: R) : Result<R>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}

