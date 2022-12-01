package otus.homework.coroutines.presentation.mvvm

import android.widget.ImageView
import android.widget.TextView
import otus.homework.coroutines.utils.ErrorDisplay

sealed class Result {

    abstract fun apply(imageView: ImageView, textView: TextView)

    class Success<T>(private val obj: T, private val uiMapper: UiMapper<T>) : Result() {
        override fun apply(imageView: ImageView, textView: TextView) {
            uiMapper.map(obj, imageView, textView)
        }
    }

    class Error(private val message: String, private val errorDisplay: ErrorDisplay) : Result() {
        override fun apply(imageView: ImageView, textView: TextView) {
            errorDisplay.showMessage(message)
        }
    }
}

