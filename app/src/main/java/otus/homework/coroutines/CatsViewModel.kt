package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import otus.homework.coroutines.api.CatsService
import otus.homework.coroutines.api.ImagesService
import otus.homework.coroutines.models.Content
import java.net.SocketTimeoutException

class CatsViewModel : ViewModel() {

    private var _catsView: ICatsView? = null

    private val catsService: CatsService = DiContainer("https://cat-fact.herokuapp.com/facts/").factService
    private val imagesService: ImagesService = DiContainer("https://aws.random.cat/").imageService

    fun onInitComplete() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            renderView(Result.Error(throwable))
        }) {
            val fact = withContext(Dispatchers.IO) {
                catsService.getCatFact()
            }
            val image = withContext(Dispatchers.IO) {
                imagesService.getCatImage()
            }
            renderView(Result.Success(Content(fact, image)))
        }

    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        viewModelScope.cancel()
    }

    private fun renderView(result: Result) {
        when (result) {
            is Result.Success<*> -> {
                if ((result.content is Content)) {
                    _catsView?.populate(
                        Content(
                            fact = result.content.fact,
                            image = result.content.image
                        )
                    )

                }
            }
            is Result.Error -> {
                when (result.throwable) {
                    is SocketTimeoutException -> {
                        _catsView?.showToast("Не удалось получить ответ от сервера")
                    }
                    else -> {
                        _catsView?.showToast(result.throwable.message.orEmpty())
                    }
                }
            }
        }
    }
}
