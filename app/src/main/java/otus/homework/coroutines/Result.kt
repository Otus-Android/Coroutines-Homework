package otus.homework.coroutines

sealed class Result {
    class Success(val uiState: UiState) : Result()
    class Error(val message: String) : Result()
}
