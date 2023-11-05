package otus.homework.coroutines.presentation

sealed interface ScreenState {

    data object Empty : ScreenState

    data object Loading : ScreenState

    data class Error(val message: String?) : ScreenState

    data class Model(
        val text: String,
        val photoUrl: String,
    ) : ScreenState
}