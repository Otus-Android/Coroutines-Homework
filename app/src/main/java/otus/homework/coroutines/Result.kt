package otus.homework.coroutines

sealed class Result  {
    class Success(val result: CatsModel): Result()
    class Error(val throwable: Throwable?, val errorMessage: String) : Result()}
