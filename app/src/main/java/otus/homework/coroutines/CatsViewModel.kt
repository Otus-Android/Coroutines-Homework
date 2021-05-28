package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel : ViewModel() {

    lateinit var catsService: CatsService
    lateinit var imageService: ImageService

    private val _catData = MutableLiveData<Result>()
    fun observableCat(): LiveData<Result> = _catData

    fun onInitComplete() {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception")
        }

        PresenterScope().launch(handler) {
            val fact = async(Dispatchers.IO) { catsService.getCatFact() }
            val image = async(Dispatchers.IO) { imageService.getImage() }

            try {
                _catData.value = Result.Success(fact.await())
                _catData.value = Result.Success(image.await())
            } catch (t: SocketTimeoutException) {
                _catData.value = Result.Error("Не удалось получить ответ от сервера")
            } catch (t: Throwable) {
                t.message?.let { _catData.value = Result.Error(it) }
                CrashMonitor.trackWarning()
            }
        }
    }
}

sealed class Result {
    data class Success<T>(val result: T) : Result()
    class Error(val errorMessage: String) : Result()
}

