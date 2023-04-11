package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val catsViewModel: CatsViewModel by viewModels {
        CatsViewModel.Factory(diContainer.factService, diContainer.imageService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

/*        catsPresenter = CatsPresenter(diContainer.factService, diContainer.imageService)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()*/

        catsViewModel.factWithImageLiveData.observe(this) { resultFactWithImage ->
            when (resultFactWithImage) {
                is Result.Success -> view.populate(resultFactWithImage.value)
                is Result.Error -> {
                    val (message, throwable) = resultFactWithImage
                    if (throwable?.javaClass == java.net.SocketTimeoutException::class.java)
                        view.showToast(R.string.socket_timeout_exception_message)
                    else message?.let { view.showToast(it + throwable) }
                }

            }
        }
        catsViewModel.requestFactAndImage()

        findViewById<Button>(R.id.button).setOnClickListener {
            catsViewModel.requestFactAndImage()
        }
    }

    override fun onStop() {
/*        catsPresenter.onStop()
        if (isFinishing) {
            catsPresenter.detachView()
        }*/
        super.onStop()
    }
}