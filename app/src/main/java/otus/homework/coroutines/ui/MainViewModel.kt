package otus.homework.coroutines.ui

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.utils.Result
import otus.homework.coroutines.api.CatsService
import otus.homework.coroutines.utils.createExceptionHandler
import otus.homework.coroutines.model.CatModel
import otus.homework.coroutines.utils.CrashMonitor
import java.lang.IllegalArgumentException
import java.net.SocketTimeoutException

class MainViewModel(private val catsService: CatsService) : ViewModel() {

    private val _catModelLiveData: MutableLiveData<Result<CatModel>> = MutableLiveData()
    val catModelLiveData: LiveData<Result<CatModel>>
        get() = _catModelLiveData

    fun handleEvent(mainEvent: MainEvent) {
        when (mainEvent) {
            is MainEvent.GetNewFactEvent -> {
                getNewFact()
            }
        }
    }

    private fun getNewFact() {
        val exceptionHandler = createExceptionHandler()
        viewModelScope.launch(exceptionHandler) {

            val factResponse = async(Dispatchers.IO) {
                catsService.getCatFact()
            }

            val picResponse = async(Dispatchers.IO) {
                catsService.getCatPicture()
            }
            _catModelLiveData.postValue(
                Result.Success(
                    CatModel(
                        factResponse.await(),
                        picResponse.await()
                    )
                )
            )
        }
    }

    private fun createExceptionHandler(): CoroutineExceptionHandler {
        return viewModelScope.createExceptionHandler {
            onFailure(it)
        }
    }

    private fun onFailure(throwable: Throwable) {
        if (throwable is SocketTimeoutException) {
            _catModelLiveData.postValue(Result.Error("Не удалось получить ответ от сервера"))
        } else {
            CrashMonitor.trackWarning()
            _catModelLiveData.postValue(Result.Error(throwable.message ?: "Unknown error"))
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val catsService: CatsService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(catsService) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}