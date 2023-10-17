package otus.homework.coroutines

sealed class Result {
    data class Success(val data: CatData) : Result()
    data class Error(val errorMessage: String) : Result()
}
