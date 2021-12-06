package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.model.CatFact
import otus.homework.coroutines.model.CatModel
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsViewModel(private val catsService: CatsService): ViewModel() {
    val catsData = MutableLiveData<Result>()
    var job: Job? = null

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning("Exception handled: ${throwable.localizedMessage}")
        viewModelScope.launch {
            when (throwable) {
                is SocketTimeoutException -> {
                    catsData.value = Error("Не удалось получить ответ от сервера")
                }
                is CancellationException -> {
                    throw throwable
                }
                else -> {
                    catsData.value = Error(throwable.message!!)
                }
            }
        }
    }

    fun loadData() {
        job = viewModelScope.launch(exceptionHandler) {
            var catFactDef = async(Dispatchers.IO) { catsService.getCatFact() }
            var catImageDef = async(Dispatchers.IO) {catsService.getCatImage()}
            val catModel = CatModel(catFactDef.await(), catImageDef.await())
            catsData.value = Success(catModel)
        }
    }

    fun cancel() {
        job?.cancel()
    }
}