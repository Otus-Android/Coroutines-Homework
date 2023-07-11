package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.R.layout
import otus.homework.coroutines.presentation.CatsView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(layout.activity_main, null) as CatsView
        setContentView(view)
    }

}
