package otus.homework.coroutines

sealed class Result
data class Error(var ex: Throwable) : Result()
data class Success(var catInfo: CatInfo) : Result()