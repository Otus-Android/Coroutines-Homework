package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.model.Result
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    lateinit var catsViewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.catsService, diContainer.picturesService)
        catsViewModel = CatsViewModel(diContainer.catsService, diContainer.picturesService)
        view.onRefresh = { catsViewModel.getCatFactAndPic() }
        catsViewModel.result.observe(this) { result ->
            when (result) {
                is Result.Success -> view.populate(result.data)
                is Result.Error -> {
                    val message = if (result.exception is SocketTimeoutException) {
                        "Не удалось получить ответ от сервером"
                    } else {
                        result.exception.message ?: "Unknown error"
                    }
                    view.showErrorToast(message)
                }
            }
        }
        catsViewModel.getCatFactAndPic()
    }

    override fun onStop() {
        catsPresenter.detachView()
        super.onStop()
    }
}
