package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class MainViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private val _resultLiveData = MutableLiveData<Result>()
    val resultLiveData: LiveData<Result>
        get() = _resultLiveData
    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning()
        _resultLiveData.postValue(Result.Error(exception.localizedMessage ?: "error"))
    }

    init {
        onInitComplete()
    }

    fun onInitComplete() {
        viewModelScope.launch(handler) {
            _resultLiveData.postValue(Result.Loading)
            try {
                val result = withContext(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                if (result.isSuccessful) {
                    _resultLiveData.postValue(Result.Success(Fact(result.body()!!.text)))
                } else throw Exception("Not successful result")
            } catch (ex: SocketTimeoutException) {
                _resultLiveData.postValue(Result.Error(ex.localizedMessage ?: "error"))
            }
        }
    }

    fun loadFactAndImage() {
        viewModelScope.launch(handler) {
            val factResult = async(Dispatchers.IO) { catsService.getCatFact() }
            val imageResult = async(Dispatchers.IO) { imageService.getCatImage() }
            _resultLiveData.postValue(Result.Loading)
            try {
                if (factResult.await().isSuccessful && imageResult.await().isSuccessful) {
                    _resultLiveData.postValue(Result.Success(Fact(factResult.await().body()!!.text, imageResult.await().body()!!.fileName)))
                } else throw Exception("Not successful result")
            } catch (ex: SocketTimeoutException) {
                _resultLiveData.postValue(Result.Error(ex.localizedMessage ?: "error"))
            }
        }
    }

    sealed class Result {
        data class Success(val data: Fact) : Result()
        data class Error(val message: String) : Result()
        object Loading : Result()
    }

}