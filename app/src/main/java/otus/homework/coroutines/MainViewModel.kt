package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class MainViewModel(
    private val catsService: CatsService,
    private val catImageService: CatImageService
) : ViewModel() {

    private var resultMutable = MutableLiveData<Result>()
    val result: LiveData<Result> = resultMutable

    private val handler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning()
        val errorMessage = when (throwable) {
            is SocketTimeoutException -> "Не удалось получить ответ от сервера"
            else -> throwable.message.toString()
        }
        resultMutable.value = Result.Error(errorMessage = errorMessage)
    }

    init {
        onInit()
    }

    fun onInit() {
        viewModelScope.launch(handler) {
            val catImage = async { catImageService.getCatImage().first().url }
            val catFact = async { catsService.getCatFact().fact }
            val catData = CatData(
                imageUrl = catImage.await(),
                fact = catFact.await()
            )
            resultMutable.value = Result.Success(catData)
        }
    }

}