package otus.homework.coroutines

sealed class Result {
  data class Success(val uiState: UiState) : Result()
  data class Error(val message: String) : Result()
}
