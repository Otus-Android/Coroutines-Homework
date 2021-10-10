package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import otus.homework.coroutines.CatsViewModel.Result.Error
import otus.homework.coroutines.CatsViewModel.Result.Success
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    sealed class Result {
        data class Success(val data: CatData) : Result()
        data class Error(val message: String) : Result()
    }

    data class CatData(val text: String, val photoUrl: String)

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    init {
        // Called on every config change, because we create it every time the main activity is (re)created.
        // For now, it suits task requirements.
        onMoreFacts()
    }

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
            _result.value = Success(CatData(getCatFact().text, getCatPhoto().url))
        }
    }
}
