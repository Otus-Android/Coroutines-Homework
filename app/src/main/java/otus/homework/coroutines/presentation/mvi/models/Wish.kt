package otus.homework.coroutines.presentation.mvi.models

/**
 * Внешние желаемые изменения состояния
 */
sealed class Wish {

    /** Загрузить кота */
    object LoadCat : Wish()
}