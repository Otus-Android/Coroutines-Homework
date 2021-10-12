package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsViewModel: ViewModel(){

    private var job: Job? = null
    private val _facts = MutableLiveData<Result>()
    private val diContainer by lazy {
        DiContainer()
    }
    val handler = CoroutineExceptionHandler { context, e ->
        _facts.value = when(e){
            is CancellationException -> Error(msgId = 0, msg = null)
            is SocketTimeoutException -> Error(msgId = R.string.server_error, msg = null)
            else -> {
                CrashMonitor.trackWarning()
                Error(0, e.message)
            }
        }
    }
    val facts: LiveData<Result>
        get() = _facts
    fun loadFacts(){
        job = viewModelScope.launch(handler) {
            val cContext = CoroutineScope(Dispatchers.IO).coroutineContext
            val defFact = async(cContext){
                diContainer.serviceFact.getCatFact()
            }
            val defPic = async(cContext) {
                diContainer.servicePic.getCatPic()
            }
            _facts.value = Success(FactAndPicture(defFact.await(), defPic.await()))
        }
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }
}

class CatsViewModelFactory: ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = CatsViewModel() as T
}

sealed class Result
data class Error(val msgId: Int, val msg: String?): Result()
data class Success(val fact: FactAndPicture): Result()