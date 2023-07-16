package otus.homework.coroutines.presentation.mvvm.owners

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.R

class CatsActivity : AppCompatActivity() {
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_view_with_custom_owners, null) as CatsView
        setContentView(view)
    }
}