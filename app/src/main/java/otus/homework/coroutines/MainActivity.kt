package otus.homework.coroutines

import android.os.Bundle
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        val viewModel = ViewModelProvider(this).get(CatsViewModel::class.java)
        viewModel.onInitComplete()
    }

    override fun onStop() {
        super.onStop()
    }
}