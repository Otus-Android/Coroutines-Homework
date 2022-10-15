package otus.homework.coroutines.error.handler

sealed class Result {
    data class Success(
        val id: Int?
    ) : Result()

    data class Error(
        val t: Throwable?
    ) : Result()
}
