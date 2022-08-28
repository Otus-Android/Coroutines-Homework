package otus.homework.coroutines.presentation

sealed class Result {
    class Success<T>(val model: T): Result()
    class Loading(val isLoading: Boolean) : Result()
    class Error(val errorMessage: String?): Result()
}

