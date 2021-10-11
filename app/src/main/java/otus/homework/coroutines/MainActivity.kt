package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private val diContainer by lazy { DiContainer(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = (layoutInflater.inflate(R.layout.activity_main, null) as CatsView).apply {
            diContainer = this@MainActivity.diContainer
        }
        setContentView(view)
    }
}