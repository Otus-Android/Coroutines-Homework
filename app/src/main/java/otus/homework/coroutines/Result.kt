package otus.homework.coroutines

sealed class Result {
    class Success(val catFact: String, val imagePath: String): Result()
    class Error(val err: String): Result()
    class Loading(): Result()
}