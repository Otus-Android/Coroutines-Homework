package otus.homework.coroutines.presentation

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.network.CatImageService
import otus.homework.coroutines.network.CatsService
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catImageService: CatImageService
) : ViewModel() {

    private val _successLiveData = MutableLiveData<Result.Success<CatModel>>()
    val successLiveData: LiveData<Result.Success<CatModel>> = _successLiveData

    private val _errorLiveData = MutableLiveData<Result.Error>()
    val errorLiveData: LiveData<Result.Error> = _errorLiveData

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    fun onInitComplete() {
        _loadingLiveData.postValue(true)

        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning()
            _loadingLiveData.postValue(false)
            //_errorLiveData.postValue(Result.Error(throwable.message))
        }) {

            val catImageDeferred = async() {
                try {
                    catImageService.getCatImage()
                } catch (e: Exception) {
                    _errorLiveData.postValue(Result.Error("Не удалось получить ответ от сервера"))
                    null
                }
            }

            val catFactDeferred = async() {
                try {
                    catsService.getCatFact()
                } catch (e: SocketTimeoutException) {
                    _errorLiveData.postValue(Result.Error("Не удалось получить ответ от сервера"))
                    null
                }
            }

            val fact = catFactDeferred.await()
            val image = catImageDeferred.await()

            val catModel = CatModel(fact, image)

            _loadingLiveData.postValue(false)
            _successLiveData.postValue(Result.Success(catModel))
        }
    }

    class Factory(
        private val catsService: CatsService,
        private val catImageService: CatImageService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
                return CatsViewModel(catsService, catImageService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}