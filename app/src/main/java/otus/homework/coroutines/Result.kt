package otus.homework.coroutines

sealed class Result {
    data class Success(val catInfo: CatInfo) : Result()
    data class Error(val message: String): Result()
}