package otus.homework.coroutines.ui.cats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
        CrashMonitor.trackWarning(throwable)
    }

    fun fetchCats() {
        viewModelScope.launch {
            try {
                val fact = async(coroutineExceptionHandler + Dispatchers.IO) { catsService.getCatFact() }
                val image = async(coroutineExceptionHandler + Dispatchers.IO) { catsService.getCatImage() }

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