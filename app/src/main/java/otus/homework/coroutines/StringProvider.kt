package otus.homework.coroutines

import androidx.annotation.StringRes

interface StringProvider {

    fun getString(@StringRes res: Int): String
}