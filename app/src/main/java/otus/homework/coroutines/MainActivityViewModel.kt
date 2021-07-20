package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException


class MainActivityViewModel(private val catsService: CatsService) : ViewModel() {
    private val _catsData = MutableLiveData<Result<CustomCatPresentationModel>>()

    val catsData: LiveData<Result<CustomCatPresentationModel>>
        get() = _catsData

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        if (e is SocketTimeoutException) _catsData.postValue(Result.Error(e))
        else {
            CrashMonitor.trackWarning(e.message)
            _catsData.postValue(Result.Error(e))
        }
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            val factResponse = withContext(Dispatchers.IO) {
                catsService.getCatFact()
            }
            val imageResponse = withContext(Dispatchers.IO) {
                catsService.getCatImage()
            }

            val fact = factResponse[0].fact
            val image = imageResponse.file

            if (!(fact.isEmpty() && image.isEmpty())) {
                val data = CustomCatPresentationModel(
                    factResponse[0].fact,
                    imageResponse.file
                )
                _catsData.postValue(Result.Success(data))
            } else {
                _catsData.postValue(
                    Result.Error(
                        RuntimeException("data from server was null or empty")
                    )
                )
            }
        }
    }
}

class MainActivityViewModelFactory(private val service: CatsService) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MainActivityViewModel(service) as T
}