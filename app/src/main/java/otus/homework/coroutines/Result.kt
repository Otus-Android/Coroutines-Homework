package otus.homework.coroutines

sealed class Result<out R> {
    data class Success<out R>(val value: R) : Result<R>()
    data class Error(val message: String?, val throwable: Throwable?) : Result<Nothing>()
}