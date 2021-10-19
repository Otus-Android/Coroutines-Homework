package otus.homework.coroutines.ui.cats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.data.model.Cat
import otus.homework.coroutines.data.model.Result
import otus.homework.coroutines.data.remote.CatsService
import otus.homework.coroutines.data.tools.CrashMonitor
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
) : ViewModel() {
    private var _catsView: ICatsView? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is CancellationException) {
            throw throwable
        } else {
            CrashMonitor.trackWarning(throwable)
        }
    }

    fun fetchCats() {
        viewModelScope.launch(coroutineExceptionHandler + Dispatchers.IO) {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { catsService.getCatImage() }

                _catsView?.load(Result.Success(Cat(fact.await(), image.await())))
            } catch (exception: Exception) {
                if (exception is SocketTimeoutException) {
                    _catsView?.load(Result.Error("Не удалось получить ответ от сервером"))
                } else {
                    throw exception
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    override fun onCleared() {
        _catsView = null
    }
}