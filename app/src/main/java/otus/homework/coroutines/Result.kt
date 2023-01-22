package otus.homework.coroutines

sealed class Result {

    class Success<T : Any>(val data: T) : Result()

    class Error(val messageId: Int? = null, val text: String? = null)
}
