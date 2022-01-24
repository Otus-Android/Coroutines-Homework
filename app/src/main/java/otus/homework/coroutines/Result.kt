package otus.homework.coroutines

sealed class Result<out T> {
    data class Success<out R>(val data: R) : Result<R>()
    data class Error(
        val msg: String?
    ) : Result<Nothing>()
}