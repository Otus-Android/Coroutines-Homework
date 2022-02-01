package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*
import otus.homework.coroutines.Result.Error
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    fun onMoreFacts() = with(catsService) {
        viewModelScope.launch(CoroutineExceptionHandler { _, ex ->
            CrashMonitor.trackWarning(ex)
            _result.value = Error(ex.message ?: "No error message")
        }) {
            supervisorScope {
                val catFact = async(Dispatchers.IO) { getCatFact().text }
                val catPhotoUrl = async(Dispatchers.IO) { getCatPhoto().url }
                try {
                    _result.postValue(Result.Success(CatData(catFact.await(), catPhotoUrl.await())))
                } catch (ex: Exception) {
                    when (ex) {
                        is SocketTimeoutException ->
                            _result.value = Error("Failed to get a response from the server")
                        else -> throw ex
                    }
                }
            }
        }
    }
}

class CatsViewModelFactory(private val catsService: CatsService) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            return CatsViewModel(catsService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

data class CatData(val text: String, val photoUrl: String)

sealed class Result {
    data class Success(val data: CatData) : Result()
    data class Error(val message: String) : Result()
}
