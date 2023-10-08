package otus.homework.coroutines.models.presentation

import android.content.Context

sealed class Text {
    abstract fun getString(context: Context): String
    data class TextByRes(val text: Int) : Text() {
        override fun getString(context: Context): String = context.getString(text)
    }

    data class TextByString(val text: String) : Text() {
        override fun getString(context: Context): String = text
    }
}
