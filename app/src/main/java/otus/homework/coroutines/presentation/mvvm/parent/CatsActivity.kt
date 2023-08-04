package otus.homework.coroutines.presentation.mvvm.parent

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.R

/**
 * `Activity` с `custom view` с информацией о случайном коте, построенной на основе паттерна `MVVM`
 */
class CatsActivity : AppCompatActivity() {

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_view_with_view_model, null) as CatsView
        setContentView(view)
    }
}