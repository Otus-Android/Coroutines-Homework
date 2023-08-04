package otus.homework.coroutines.presentation.mvi.models

import otus.homework.coroutines.domain.models.Cat

/** Модель состояния с информацией о кошке */
sealed class State {

    /** Состояние бездействия */
    object Idle : State()

    /**
     * Состояние наличия информации о кошке
     *
     * @property cat информация о кошке
     */
    data class Success(val cat: Cat) : State()
}