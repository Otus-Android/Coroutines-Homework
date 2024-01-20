package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private var _catsView: ICatsView? = null


    var state: Result = Result.Init

    private lateinit var fact: Deferred<Fact>
    private lateinit var image: Deferred<Image>
    fun onInitComplete() {
        viewModelScope.launch {
            try {
                state = Result.Init
                fact = async { catsService.getCatFact() }
                image = async { imageService.getCatImage().first() }

                state = Result.Success("success")
                _catsView?.populate(fact.await(), image.await())

            } catch (e: SocketTimeoutException) {
                _catsView?.toastError(e)
             } catch (e: CancellationException) {
                 _catsView?.toastError(e)
            } catch (e: Exception) {
                state = Result.Error
                _catsView?.toastError(e)
                CrashMonitor.trackWarning(e)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

}

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val imageService: ImageService
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatsViewModel(catsService, imageService) as T
}