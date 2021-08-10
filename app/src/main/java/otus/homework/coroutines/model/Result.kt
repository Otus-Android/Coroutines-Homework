package otus.homework.coroutines.model

sealed class Result {
    data class Success(val cats: Cats) : Result()
    data class Error(val throwable: Throwable) : Result()
}
