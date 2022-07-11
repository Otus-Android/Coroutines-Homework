package otus.homework.coroutines

sealed class Result{
    data class Success(val data: Fact) : Result()
    data class Error(val message: String) : Result()
    object Loading : Result()
}
