package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel : ViewModel() {
    private var _catsLiveData = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catsLiveData
    val diConteiner = DiContainer()

    companion object {
        private const val TAG = "CatsViewModel"
    }

    //    private val presenterScope = PresenterScope()
    val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, e -> CrashMonitor.trackWarning(TAG, e) }


    fun onInitComplete() {

        val job = viewModelScope.launch(coroutineExceptionHandler) {
            val jobImage =
                async {
                    var catImage = CatsImage("??")
                    try {
                        catImage = diConteiner.serviceImage.getCatImage()
                    } catch (e: SocketTimeoutException) {
                        _catsLiveData.value =
                            Result.Error(e, "Не удалось получить ответ от сервера")
                    }
                    catImage
                }

            val jobFact = async {
                diConteiner.service.getCatFact()
            }
            _catsLiveData.value = Result.Success(CatsModel(jobFact.await(), jobImage.await()))
        }
    }
}
