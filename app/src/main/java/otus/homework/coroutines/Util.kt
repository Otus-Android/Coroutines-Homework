package otus.homework.coroutines

import android.widget.Toast

object Util {
    fun showToast(text: String) {
        Toast.makeText(App.INSTANCE?.applicationContext, text, Toast.LENGTH_LONG).show()
    }
    fun showToast(id: Int) {
        Toast.makeText(App.INSTANCE?.applicationContext, App.INSTANCE?.applicationContext?.getString(id), Toast.LENGTH_LONG).show()
    }
}