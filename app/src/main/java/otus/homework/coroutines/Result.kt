package otus.homework.coroutines

sealed class Result<T>(
    open val data: T? = null,
    open val message: T? = null
) {
    class Success<T>(data: T?) : Result<T>(data)
    class Error<T>(
        override val data: T? = null,
        override val message: T?
    ) : Result<T>(data, message)
}
