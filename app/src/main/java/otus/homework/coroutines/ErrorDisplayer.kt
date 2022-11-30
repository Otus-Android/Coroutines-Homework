package otus.homework.coroutines

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast

interface ErrorDisplay {
    fun showMessage(message: String)

    class Base(private val context: Context) : ErrorDisplay {
        override fun showMessage(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}