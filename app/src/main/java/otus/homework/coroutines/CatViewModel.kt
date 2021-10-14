package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatViewModel(private val catsService: CatsService = DiContainer.service) : ViewModel() {

    private var job: Job? = null

    private val stateLiveData = MutableLiveData<Result<CatUiState>>()
    val state: LiveData<Result<CatUiState>> = stateLiveData

    private val handler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is SocketTimeoutException -> stateLiveData.postValue(Result.Error("Не удалось получить ответ от сервера"))
            else -> stateLiveData.postValue(Result.Error("Упс, произошла ошибка: ${exception.message}"))
        }

        CrashMonitor.trackWarning(exception)
    }

    fun onStart() {
        requestFact()
    }

    fun onStop() {
        job?.cancel()
        job = null
    }

    fun requestFact() {
        if (job != null && job!!.isActive)
            return

        job = viewModelScope.launch(handler) {
            val fact = async(Dispatchers.IO) {
                catsService.getCatFact()
            }

            val image = async(Dispatchers.IO) {
                catsService.getCatImage()
            }

            val state = Result.Success(CatUiState(fact.await(), image.await()))

            stateLiveData.postValue(state)
        }
    }
}
