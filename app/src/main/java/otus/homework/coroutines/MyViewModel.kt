package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.Result.*
import java.net.SocketTimeoutException

class MyViewModel() : ViewModel() {

    private var diContainer:DiContainer? = null
    private var diContainerPicture:DiContainerPicture? = null

    var _factAndPicture = MutableLiveData<Result<FactAndPicture>>()
    val factAndPicture: LiveData<Result<FactAndPicture>>
        get() = _factAndPicture

    init {
        diContainer = DiContainer()
        diContainerPicture = DiContainerPicture()
    }

    fun onInitComplete() {

        try {
            var factIn = Fact()
            var urlPictureIn = listOf<UrlPicture>()
            val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
                CrashMonitor.trackWarning()
                _factAndPicture?.value = Result.Error("Не удалось получить ответ от сервером")
            }
            val coroutineContext = Dispatchers.Main+ CoroutineName("CatsCoroutine")+coroutineExceptionHandler
            viewModelScope.launch (coroutineContext){
                val jobFact = launch { factIn = diContainer?.service?.getCatFact()!!}
                val jobUrlPicture = launch { urlPictureIn = diContainerPicture?.servicePicture?.getCatPictureUrl()!!}
                jobFact.join()
                jobUrlPicture.join()
                _factAndPicture?.value = Result.Success(FactAndPicture(factIn.fact, urlPictureIn[0].url))
            }
        }catch(e: SocketTimeoutException){
            _factAndPicture?.value = Result.Error("Не удалось получить ответ от сервером")
            CrashMonitor.trackWarning()
        }

    }

    override fun onCleared() {
        viewModelScope.cancel()
    }
}