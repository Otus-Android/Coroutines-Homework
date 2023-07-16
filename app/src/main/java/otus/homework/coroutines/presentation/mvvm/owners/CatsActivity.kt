package otus.homework.coroutines.presentation.mvvm.owners

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import otus.homework.coroutines.R

/**
 * `Activity` с `custom view` с информацией о случайном коте, построенной на основе применения `ViewModel
 * и `custom` реализаций [ViewModelStoreOwner] и [LifecycleOwner]
 */
class CatsActivity : AppCompatActivity() {
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_view_with_custom_owners, null) as CatsView
        setContentView(view)
    }
}