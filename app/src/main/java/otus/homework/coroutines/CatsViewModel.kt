package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(private val catsService: CatsService) : ViewModel() {

    val toastLD = MutableLiveData<String>()
    val catsPopulationLD = MutableLiveData<CatModel>()
    private val imageSource = "https://aws.random.cat/meow"

    private val job = Job()
    private val coroutineName = CoroutineName("CatsCoroutine")
    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception.message ?: "ERROR!")
    }

    fun onInitComplete() {
        viewModelScope.launch(job + coroutineName + handler) {
            when (val result = doTask()) {
                is Result.Success<*> ->
                    catsPopulationLD.postValue(result.model as? CatModel)
                is Result.Error -> {
                    if (result.e is SocketTimeoutException)
                        toastLD.postValue("")
                    else {
                        result.e.message?.let {
                            CrashMonitor.trackWarning(it)
                            toastLD.postValue(it)
                        }
                    }
                }
            }
        }
    }

    private suspend fun doTask() = withContext(Dispatchers.IO) {
        try {
            val imageResponse = async(Dispatchers.IO) {
                catsService.getCatImage(imageSource)
            }
            val factResponse = async(Dispatchers.IO) {
                catsService.getCatFact()
            }
            val imageBody = imageResponse.await().body()
            val factBody = factResponse.await().body()
            if (imageBody != null && factBody != null)
                Result.Success(CatModel(factBody.fact, imageBody.url))
            else Result.Error(Exception())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}