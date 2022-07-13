package otus.homework.coroutines

sealed class Result {
    class Success(val data: IllustratedFact): Result()
    class Error(val messageText: String?, val messageResource: Int?) : Result()
}