package otus.homework.coroutines.view

import android.content.Context
import android.widget.Toast

class Toaster(private val context: Context) {

    fun showToast(messageRes: Int) {
        showToast(context.resources.getString(messageRes))
    }

    fun showToast(messageText: String) {
        Toast.makeText(context, messageText, Toast.LENGTH_LONG).show()
    }

}
