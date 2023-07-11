package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.R.layout
import otus.homework.coroutines.presentation.CatsView
import otus.homework.coroutines.presentation.CatsViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(layout.activity_main, null) as CatsView
        setContentView(view)

        ViewModelProvider(this)[CatsViewModel::class.java]
    }
}
