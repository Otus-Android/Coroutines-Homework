package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class CatsViewModel(private val service: CatsService) : ViewModel() {

    private val catsInfo = MutableLiveData<Result>()
    private var job: Job? = null

    fun fetchCatsInfo() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.logThrowable(throwable)
            throwable.message?.let {
                catsInfo.value = Error(it)
            }
        }) {
            try {
                coroutineScope {
                    val fact = async(Dispatchers.IO) { service.getCatFact() }
                    val image = async(Dispatchers.IO) { service.getCatImage() }

                    catsInfo.value = Success((CatsInfo(fact.await().text, image.await().file)))
                }
            } catch (e: java.net.SocketTimeoutException) {
                catsInfo.value = Error("Не удалось получить ответ от сервера")
            }
        }
    }
    fun loadCatsInfo(): MutableLiveData<Result> {
        return catsInfo
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}