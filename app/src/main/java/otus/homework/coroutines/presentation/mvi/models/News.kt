package otus.homework.coroutines.presentation.mvi.models


/**
 * `Single-shot event`-ы, не входящие в состояние
 */
sealed class News {

    /**
     * Ошибка загрузки кота
     *
     * @property message сообщение об ошибке
     */
    data class ErrorLoadingCat(val message: String) : News()
}