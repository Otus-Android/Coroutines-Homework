package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private var job: Job? = null
    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result>
        get() = _result

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        CrashMonitor.trackWarning(e)
        val errorMessage = when (e) {
            is SocketTimeoutException -> exceptionMessage
            else -> e.message
        }
        _result.value = Result.Error(errorMessage = errorMessage)
    }

    init {
        onInitCompleted()
    }

    fun onInitCompleted() {
        job =
            viewModelScope.launch(CoroutineName("CatsCoroutine") + exceptionHandler) {
                val deferredFact = async { catsService.getCatFact() }
                val deferredImage = async { imageService.getImage() }

                val fact = deferredFact.await().fact
                val image = deferredImage.await().body()!![0].url

                val catInfoValue = ImagedFact(fact, image)

                _result.value = Result.Success(data = catInfoValue)
            }
    }

    private val exceptionMessage = "Не удалось получить ответ от сервера"
}
