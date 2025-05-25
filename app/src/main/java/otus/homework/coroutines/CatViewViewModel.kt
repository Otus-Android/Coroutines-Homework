package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatViewViewModel(
    private val catsService: CatsService,
    private val photoService: PhotoService,
) : ViewModel() {

    private val _catsLiveData = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catsLiveData
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning()
        _catsLiveData.value =
            Error(
                if (throwable is SocketTimeoutException) TIME_OUT
                else throwable.message ?: throwable.toString()
            )
    }

    init {
        onInitComplete()
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            coroutineScope {
                val fact = async(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                val photo = async(Dispatchers.IO) {
                    photoService.getPhoto()
                }
                val factWithPhoto = FactWithPhoto(
                    fact = fact.await(),
                    photo = photo.await().first().url
                )
                _catsLiveData.value = Success(factWithPhoto)
            }
        }
    }

    companion object {
        const val TIME_OUT = "Не удалось получить ответ от сервера"
    }
}

sealed class Result
data class Success(val fact: FactWithPhoto) : Result()
data class Error(val message: String) : Result()