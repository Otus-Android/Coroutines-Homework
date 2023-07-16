package otus.homework.coroutines.models.presentation

import otus.homework.coroutines.models.domain.Cat

/**
 * Модель состояния с информацией о кошке
 */
sealed class CatUiState {

    /** Состояние бездействия */
    object Idle : CatUiState()

    /**
     * Состояние наличия информации о кошке
     *
     * @param cat информация о кошке
     */
    data class Success(val cat: Cat) : CatUiState()

    /**
     * Состояние отсутствия данных о кошке
     *
     * @param message описание причины отсутствия
     * @param isShown признак отображения причины отсутствия данных
     */
    data class Error(val message: String, val isShown: Boolean = false) : CatUiState()
}