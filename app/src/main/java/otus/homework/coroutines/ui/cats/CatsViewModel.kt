package otus.homework.coroutines.ui.cats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
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
        if (throwable is SocketTimeoutException) {
            _catsView?.load(Result.Error("Не удалось получить ответ от сервером"))
        } else {
            CrashMonitor.trackWarning(throwable)
        }
    }

    fun fetchCats() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val fact = catsService.getCatFact()
            val image = catsService.getCatImage()

            _catsView?.load(Result.Success(Cat(fact, image)))
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}