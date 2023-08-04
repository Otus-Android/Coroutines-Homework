package otus.homework.coroutines.presentation.mvi

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.R

/**
 * `Activity` с `custom view` информации о коте, построеной на основе паттерна `MVI` и использования `MVICore`
 */
class CatsActivity : AppCompatActivity() {

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_view_with_mvi, null) as CatsView
        setContentView(view)
    }
}