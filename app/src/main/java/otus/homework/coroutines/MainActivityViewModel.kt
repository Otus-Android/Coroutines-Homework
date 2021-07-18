package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.NullPointerException
import java.net.SocketTimeoutException
import kotlin.coroutines.coroutineContext


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
            getData()
        }
    }

    private suspend fun getData() {
        val fact = withContext(Dispatchers.IO) {
            catsService.getCatFact()
        }
        val image = withContext(Dispatchers.IO) {
            catsService.getCatImage()
        }

        if (!fact.body()?.get(0)?.fact.isNullOrEmpty()
            && !image.body()?.file.isNullOrEmpty()) {
            val data = CustomCatPresentationModel(
                fact.body()!![0].fact,
                image.body()!!.file
            )
            _catsData.postValue(Result.Success(data))
        } else {
            _catsData.postValue(Result.Error(
                NullPointerException("data from server was null or empty")
            ))
        }
    }
}

class MainActivityViewModelFactory(private val service: CatsService) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MainActivityViewModel(service) as T
}