package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    val resultData: LiveData<Result<Exception, Cat>>
        get() = _resultFlow
    private val _resultFlow = MutableLiveData<Result<Exception, Cat>>()

    fun onInitComplete() {
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            CrashMonitor.trackWarning()
        }
        viewModelScope.launch(exceptionHandler) {
            val result = try {
                Success(
                    Cat(
                        catsService.getCatFact().text,
                        catsService.getCatImage().file
                    )
                )
            } catch (e: Exception) {
                Error(e)
            }
            _resultFlow.postValue(result)
        }
    }

    class CatsViewModelFactory(private val service: CatsService) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = CatsViewModel(service) as T
    }
}