package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService, private val imagesService: ImagesService
) : ViewModel() {


    private var _catsView: ICatsView? = null

    private val exceptionHandler = CoroutineExceptionHandler { context, error ->
        when (error) {
            is SocketTimeoutException -> {
                _catsView?.message("Не удалось получить ответ от сервером")
            }
            else -> {
                CrashMonitor.trackWarning()
            }
        }
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                val fact = async {
                    catsService.getCatFact()
                }
                val image = async {
                    imagesService.getCatImg()
                }
                _catsView?.populate(Fact(fact.await()), Img(image.await()))
            }
        }
    }


    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    override fun onCleared() {
        super.onCleared()
        _catsView = null
    }
}