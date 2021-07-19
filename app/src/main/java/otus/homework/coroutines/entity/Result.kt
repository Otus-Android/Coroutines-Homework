package otus.homework.coroutines.entity

sealed class Result {

    data class Success(
        val animal: Animal
    ) : Result()

    class Error(
        val throwable: Throwable
    ) : Result()
}