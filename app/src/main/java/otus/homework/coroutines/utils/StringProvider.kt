package otus.homework.coroutines.utils

import androidx.annotation.StringRes

/**
 * Поставщик строковых значений
 */
interface StringProvider {

    /** Получить строковое представление на основе идентификатора [res] */
    fun getString(@StringRes res: Int): String

    /** Получить строковое представление на основе идентификатора [res] и `placeholders` [args] */
    fun getString(@StringRes res: Int, vararg args: Any): String
}