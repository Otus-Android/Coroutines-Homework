package otus.homework.coroutines.models.presentation

import otus.homework.coroutines.models.domain.Cat

sealed class CatUiState {

    object Idle : CatUiState()

    data class Success(val cat: Cat) : CatUiState()

    data class Error(val message: String, val isShown: Boolean = false) : CatUiState()
}