package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsViewModel(private val diContainer: DiContainer): ViewModel(){

    private var job: Job? = null
    private val _facts = MutableLiveData<Result>()

    val handler = CoroutineExceptionHandler { context, e ->
        CrashMonitor.trackWarning()
        _facts.value = Error(0, e.message)
    }
    val facts: LiveData<Result>
        get() = _facts
    fun loadFacts(){
        viewModelScope.launch(handler) {
            val defFact = async(Dispatchers.IO) {
                diContainer.serviceFact.getCatFact()
            }
            val defPic = async(Dispatchers.IO) {
                diContainer.servicePic.getCatPic()
            }
            try {
                _facts.value = Success(FactAndPicture(defFact.await(), defPic.await()))
            } catch (e: SocketTimeoutException){
                _facts.value = Error(msgId = R.string.server_error, msg = null)
            }
        }
    }
}

class CatsViewModelFactory(private val di: DiContainer): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = CatsViewModel(di) as T
}

sealed class Result
data class Error(val msgId: Int, val msg: String?): Result()
data class Success(val fact: FactAndPicture): Result()