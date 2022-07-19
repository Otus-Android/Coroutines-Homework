package otus.homework.coroutines.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.di.DiContainer
import java.net.SocketTimeoutException

class CatsViewModel: ViewModel() {

    private val _successLiveData = MutableLiveData<Result.Success<CatModel>>()
    val successLiveData: LiveData<Result.Success<CatModel>> = _successLiveData

    private val _errorLiveData = MutableLiveData<Result.Error>()
    val errorLiveData: LiveData<Result.Error> = _errorLiveData

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private val diContainer = DiContainer()
    private val catsService = diContainer.service
    private val catImageService = diContainer.imageCatsService

    fun onInitComplete() {
        _loadingLiveData.postValue(true)

        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning()
            _loadingLiveData.postValue(false)
            _errorLiveData.postValue(Result.Error(throwable.message))
        }) {

            val catImageDeferred = async(Job()) {
                catImageService.getCatImage()
            }

            val catFactDeferred = async(Job()) {
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
}