package otus.homework.coroutines

sealed class CatUiState {
    data class Success(val data: CatModel) : CatUiState()
    data class Error(val error : Throwable) : CatUiState()
}
