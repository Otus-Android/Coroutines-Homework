package otus.homework.coroutines

sealed class Result
data class Error(val t: Throwable?) : Result()
object Empty : Result()
data class Success(val data: AnimalCard) : Result()
