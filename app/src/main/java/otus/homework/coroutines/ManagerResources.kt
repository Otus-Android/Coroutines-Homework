package otus.homework.coroutines

import android.content.Context
import android.support.annotation.StringRes

interface ManagerResources {
    fun getString(@StringRes id: Int): String

    class Base(private val context: Context) : ManagerResources {
        override fun getString(id: Int) = context.getString(id)
    }
}