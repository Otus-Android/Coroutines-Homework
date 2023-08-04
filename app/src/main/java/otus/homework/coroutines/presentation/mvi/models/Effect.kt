package otus.homework.coroutines.presentation.mvi.models

import otus.homework.coroutines.domain.models.Cat

/**
 * Внутреннее изменения состояния
 */
sealed class Effect {

    /**
     * Загрузка начата
     */
    object StartedLoading : Effect()

    /**
     * Успешно загружено
     *
     * @property cat информация о кошке
     */
    data class SuccessfulLoaded(val cat: Cat) : Effect()

    /**
     * Ошибка загрузки
     *
     * @property message сообщение об ошибке
     */
    data class ErrorLoading(val message: String) : Effect()
}