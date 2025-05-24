package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageCatsService: ImageCatsService
) : ViewModel() {

    private val _catsLiveData = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catsLiveData

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, t ->
        when (t) {
            is CancellationException -> throw t
            is SocketTimeoutException -> _catsLiveData.value =
                Error("Не удалось получить ответ от сервера")

            else -> {
                _catsLiveData.value = Error(t.message ?: "Неизвестная ошибка")
                CrashMonitor.trackWarning()
            }
        }
    }

    init {
        getCat()
    }

    fun getCat() {
        viewModelScope.launch(coroutineExceptionHandler) {
            coroutineScope {
                val fact = async { catsService.getCatFact() }
                val image = async { imageCatsService.getImageCatFact().first() }
                _catsLiveData.value = Success(Cat(fact.await(), image.await()))
            }
        }
    }
}

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val imageService: ImageCatsService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = modelClass.getConstructor(
        CatsService::class.java,
        ImageCatsService::class.java
    ).newInstance(catsService, imageService)
}

sealed class Result
data class Success(val cat: Cat) : Result()
data class Error(val message: String) : Result()