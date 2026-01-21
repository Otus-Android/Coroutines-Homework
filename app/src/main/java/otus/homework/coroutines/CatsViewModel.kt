package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private val _catsState = MutableLiveData<Result<CatPresentationModel>?>()
    val catsState: LiveData<Result<CatPresentationModel>?> = _catsState

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)

        val errorMessage = when (throwable) {
            is SocketTimeoutException -> "Не удалось получить ответ от сервера"
            else -> throwable.message ?: "Произошла ошибка"
        }

        _catsState.postValue(Result.Error(errorMessage, throwable))
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            _catsState.postValue(null)

            val factDeferred = async(Dispatchers.IO) {
                catsService.getCatFact()
            }
            val imageDeferred = async(Dispatchers.IO) {
                catsService.getCatImage().first()
            }
            val fact = factDeferred.await()
            val image = imageDeferred.await()

            val model = CatPresentationModel(
                fact = fact.fact,
                imageUrl = image.url
            )
            _catsState.postValue(Result.Success(model))
        }
    }

}