package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

/**
 * @author Юрий Польщиков on 11.07.2021
 */
class CatsViewModel(
    private val catsFactService: CatsService,
    private val catsImageService: CatsImageService
) : ViewModel() {

    private var _catsView: ICatsView? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        when (e) {
            is SocketTimeoutException -> _catsView?.showError(Result.Error(null))
            else -> {
                CrashMonitor.trackWarning()
                _catsView?.showError(Result.Error(e.message))
            }
        }
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            val factImageDeferred = async(Dispatchers.IO) { catsImageService.getCatImage() }
            val factsDeferred = async(Dispatchers.IO) { catsFactService.getCatFact() }
            val factImage = factImageDeferred.await().file
            val facts = factsDeferred.await()
            val fact = if (facts.isEmpty()) throw IllegalArgumentException("empty fact") else facts[0].fact

            _catsView?.populate(Result.Success(PresentationFact(fact, factImage)))
        }
    }

    fun attachView(view: CatsView) {
        _catsView = view
    }

    override fun onCleared() {
        _catsView = null
    }
}