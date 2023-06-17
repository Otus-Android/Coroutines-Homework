package otus.homework.coroutines

sealed interface Result

data class Success(val catData: CatData): Result
data class Error(val message: String) : Result