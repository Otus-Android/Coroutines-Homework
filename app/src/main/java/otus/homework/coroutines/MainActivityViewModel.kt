package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*
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
            val factResponse: Deferred<List<Fact>> = async (Dispatchers.IO) {
                println("debugMyApp: fact api start")
                catsService.getCatFact()
            }

            val imageResponse: Deferred<CatPicture> = async (Dispatchers.IO) {
                println("debugMyApp: image api start")
                catsService.getCatImage()
            }

            val fact = factResponse.await()
            println("debugMyApp: fact results awaited, result = ${fact[0].fact}")
            val image = imageResponse.await().file
            println("debugMyApp: image results awaited, result = $image")

            if (fact.isNotEmpty() && image.isNotEmpty()) {
                val data = CustomCatPresentationModel(
                    fact[0].fact,
                    image
                )
                _catsData.postValue(Result.Success(data))
            } else {
                _catsData.postValue(
                    Result.Error(
                        RuntimeException("received data from server was empty")
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