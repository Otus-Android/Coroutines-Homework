package otus.homework.coroutines.presentation

sealed class Result {
    class Success<T>(val model: T): Result()
    class Error(val errorMessage: String?): Result()
}

