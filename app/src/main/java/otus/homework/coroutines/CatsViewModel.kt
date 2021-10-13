package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CatsViewModel.Result.Error
import otus.homework.coroutines.CatsViewModel.Result.Success
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    class CatsViewModelFactory(private val catsService: CatsService) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
                return CatsViewModel(catsService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    sealed class Result {
        data class Success(val data: CatData) : Result()
        data class Error(val message: String) : Result()
    }

    data class CatData(val text: String, val photoUrl: String)

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    fun onMoreFacts() = with(catsService) {
        viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
            when (exception) {
                is SocketTimeoutException ->
                    _result.value = Error("Failed to get a response from the server")
                else -> {
                    CrashMonitor.trackWarning(exception)
                    _result.value = Error(exception.message ?: "No error message")
                }
            }
        }) {
            val catFact = async { getCatFact().text }
            val catPhoto = async { getCatPhoto().url }
            _result.value = Success(CatData(catFact.await(), catPhoto.await()))
        }
    }
}
