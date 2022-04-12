package otus.homework.coroutines

sealed class Result {

    data class Success<T>(
        val data: T
    ) : Result()

    object Error : Result()
}
