package otus.homework.coroutines.presentation.mvvm.parent.models

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

    /**
     * Состояние отсутствия данных о кошке
     *
     * @property message описание причины отсутствия
     * @property isShown признак отображения причины отсутствия данных
     */
    data class Error(val message: String, val isShown: Boolean = false) : CatUiState()
}