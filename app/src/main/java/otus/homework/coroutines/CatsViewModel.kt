package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private var _catsView: ICatsView? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception.message)
    }

    fun onInitComplete() {
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                coroutineScope {
                    val catFact = async(Dispatchers.IO) { catsService.getCatFact() }
                    val catImage = async(Dispatchers.IO) { catsService.getCatImage() }
                    val state = CatsScreenState.Success(
                        CatInfo(
                            catFact = catFact.await().fact,
                            catImageUrl = catImage.await().first().url
                        )
                    )
                    _catsView?.populate(state)
                }
            } catch (e: SocketTimeoutException) {
                _catsView?.populate(CatsScreenState.Error(R.string.error_message, e))
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }


    fun detachView() {
        _catsView = null
        viewModelScope.cancel()
    }
}

class CatsViewModelFactory(private val catsService: CatsService) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsService) as T
}