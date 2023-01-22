package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(private val catsService: CatsService) : ViewModel() {

    val toastLD = MutableLiveData<String>()
    private val _catsPopulationLD = MutableLiveData<CatModel>()
    val catsPopulationLD: LiveData<CatModel> = _catsPopulationLD
    private val imageSource = "https://aws.random.cat/meow"

    private val coroutineName = CoroutineName("CatsCoroutine")
    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception.message ?: "ERROR!")
    }

    fun onInitComplete() {
        viewModelScope.launch(coroutineName + handler) {
            when (val result = doTask()) {
                is Result.Success<*> ->
                    _catsPopulationLD.postValue(result.model as? CatModel)
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

    private suspend fun doTask(): Result {
        return try {
            val imageResponse = viewModelScope.async(Dispatchers.IO) {
                catsService.getCatImage(imageSource)
            }
            val factResponse = viewModelScope.async(Dispatchers.IO) {
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