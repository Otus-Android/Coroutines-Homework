package otus.homework.coroutines.presentation.viewmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.R

class ViewWithViewModelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_view_with_view_model, null) as CatsViewWithViewModel
        setContentView(view)
    }
}