package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class CatsViewModel : ViewModel() {
    var catsService: CatsService? = null

    private val _fact: MutableLiveData<FactModel> = MutableLiveData()
    val fact: LiveData<FactModel>
        get() = _fact

    private var currentRequest: Job? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        if (exception is java.net.SocketTimeoutException)
            CrashMonitor.trackWarning("Не удалось получить ответ от сервера")
        else {
            CrashMonitor.trackWarning("CoroutineExceptionHandler got $exception.message")
        }
    }

    fun onInitComplete() {
        currentRequest = viewModelScope.launch(handler) {
            val fact = async { getFact() }
            val image = async { getRandomImage() }
            _fact.value = FactModel(fact.await()?.text, image.await()?.file)
        }
    }

    private suspend fun getFact(): Fact? {
        return withContext(handler) {
            catsService?.getCatFact()
        }
    }

    private suspend fun getRandomImage(): Image? {
        return withContext(handler) {
            catsService?.getRandomImage()
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelRequest()
    }

    private fun cancelRequest() {
        currentRequest?.cancel()
    }
}

