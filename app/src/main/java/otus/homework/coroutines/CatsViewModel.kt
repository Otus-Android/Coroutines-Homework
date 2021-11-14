package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.models.PresentModel
import otus.homework.coroutines.services.ImageService
import otus.homework.coroutines.utils.ResultCats
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private var _catsView: ICatsView? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception)
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            supervisorScope {
                try {
                    val requestFact = async { catsService.getCatFact() }
                    val requestImage = async { imageService.getCatImage() }
                    val responseFact = requestFact.await()
                    val responseImage = requestImage.await()
                    _catsView?.populate(PresentModel(responseImage, responseFact))
                } catch (cause: Exception) {
                    when (cause) {
                        is SocketTimeoutException -> _catsView?.showResultCat(ResultCats.Error(cause))
                        else -> throw cause
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _catsView = null
    }
}

