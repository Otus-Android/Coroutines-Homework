package otus.homework.coroutines

sealed class Result {

    class Success<T>(
        val response: T
    ) : Result()

    class Error(
        val message: String
    ) : Result()
}