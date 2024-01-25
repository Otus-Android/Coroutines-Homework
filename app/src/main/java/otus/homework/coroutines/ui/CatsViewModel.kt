package otus.homework.coroutines.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import otus.homework.coroutines.data.CatsRepository
import otus.homework.coroutines.util.CrashMonitor
import java.net.SocketTimeoutException

class CatsViewModel(private val catsRepository: CatsRepository): ViewModel() {

    private var _catsView: ICatsView? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _catsView?.render(Result.Error(exception))
        if (exception !is SocketTimeoutException) {
            CrashMonitor.trackWarning()
        }
    }

    fun onInitComplete() {
        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            withContext(Dispatchers.IO) {
                val catsData = catsRepository.getCatsData()
                withContext(Dispatchers.Main) {
                    _catsView?.render(Result.Success(catsData))
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelJob() {
        viewModelScope.cancel()
    }

    class Factory(private val repository: CatsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatsViewModel(repository) as T
        }
    }

}