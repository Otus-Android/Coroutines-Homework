package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import otus.homework.coroutines.Result.*
import java.lang.RuntimeException
import java.net.SocketTimeoutException

class MyViewModel(private val diContainer: CatsService, private val diContainerPicture: CatsServicePicture) : ViewModel() {

    private val _factAndPicture = MutableLiveData<Result<FactAndPicture>>()
    val factAndPicture: LiveData<Result<FactAndPicture>>
        get() = _factAndPicture

    fun onInitComplete() {

        try {

            val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
                CrashMonitor.trackWarning()
                _factAndPicture?.value = Result.Error("Не удалось получить ответ от сервером")
            }
            val coroutineContext = CoroutineName("CatsCoroutine")+coroutineExceptionHandler
            viewModelScope.launch (coroutineContext){
                val jobFact: Deferred<Fact> = async {
                    diContainer.getCatFact()!!}
                val jobUrlPicture: Deferred<List<UrlPicture>> = async {
                    diContainerPicture.getCatPictureUrl()!!}

                _factAndPicture?.value = Result.Success(FactAndPicture(jobFact.await().fact, jobUrlPicture.await()[0].url))
            }
        }catch(e: SocketTimeoutException){
            _factAndPicture?.value = Result.Error("Не удалось получить ответ от сервером")
            CrashMonitor.trackWarning()
        }

    }

    class MyViewModelFactory(private val diContainer: CatsService, private val diContainerPicture: CatsServicePicture) :
        ViewModelProvider.NewInstanceFactory() {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MyViewModel::class.java)) {
                return MyViewModel(diContainer, diContainerPicture) as T
            }
            throw RuntimeException("Не известная вью модель $modelClass !")
        }
    }

}