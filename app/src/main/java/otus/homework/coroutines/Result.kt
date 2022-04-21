package otus.homework.coroutines

sealed class Result
data class Success(val fact: FactAndImage) : Result()
data class Error(val error: String?) : Result()
