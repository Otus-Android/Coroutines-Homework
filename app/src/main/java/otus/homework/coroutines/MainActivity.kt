package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    val presenterScope = PresenterScope()
    lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)

        job = presenterScope.launch {
            try {
                catsPresenter.onInitComplete()
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    Toast.makeText(
                        this@MainActivity,
                        "Не удалось получить ответ от сервером",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    Log.e("error", e.printStackTrace().toString())
                }
            }
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
            job.cancel()
        }
        super.onStop()
    }
}