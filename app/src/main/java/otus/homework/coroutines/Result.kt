package otus.homework.coroutines

sealed class Result{
    class Success(val t: Any) : Result()
    class Failure(val exception: String) : Result()
}
