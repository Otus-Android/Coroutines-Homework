package otus.homework.coroutines.presentation.mvvm.owners.models

import otus.homework.coroutines.domain.models.Cat

/**
 * Модель состояния с информацией о кошке
 */
sealed class CatUiState {

    /** Состояние бездействия */
    object Idle : CatUiState()

    /**
     * Состояние наличия информации о кошке
     *
     * @property cat информация о кошке
     */
    data class Success(val cat: Cat) : CatUiState()
}