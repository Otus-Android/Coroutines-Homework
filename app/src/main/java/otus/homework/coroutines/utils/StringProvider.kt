package otus.homework.coroutines.utils

import androidx.annotation.StringRes

interface StringProvider {

    fun getString(@StringRes res: Int): String
}