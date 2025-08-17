package otus.homework.coroutines

sealed interface Result {
    data class Success(val uiModel: CatsUiModel) : Result
    data class Error(val message: String?) : Result
    object Loading : Result
}