package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.models.Content
import java.net.SocketTimeoutException

class CatsViewModel : ViewModel() {

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    private var _catsView: ICatsView? = null

    private val diContainer = DiContainer()

    fun onInitComplete() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning(throwable.message.orEmpty())
        }) {
            try {
                val fact = async {
                    diContainer.factService.getCatFact()
                }
                val image = async {
                    diContainer.imageService.getCatImage()
                }
                _result.value = Result.Success(Content(fact.await(), image.await()))
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            }
        }
    }
}
