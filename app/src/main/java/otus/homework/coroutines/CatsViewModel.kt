package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    companion object {

        fun provideFactory(
            catsService: CatsService,
            imageService: ImageService
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CatsViewModel(catsService, imageService) as T
                }
            }
    }

    private val _catLiveData = MutableLiveData<Result.Success<Response>>()
    val catLiveData: LiveData<Result.Success<Response>>
        get() = _catLiveData

    private val _toastMessage = MutableLiveData<Result.Error>()
    val toastMessage: LiveData<Result.Error>
        get() = _toastMessage

    private val handler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)
    }

    fun onInitComplete() {
        viewModelScope.launch(context = handler) {
            try {
                supervisorScope {
                    val fact = async {
                        catsService.getCatFact()
                    }
                    val image = async { imageService.getCatImage() }
                    _catLiveData.value = Result.Success(
                        response = Response(
                            image = image.await().file,
                            fact = fact.await().fact
                        )
                    )
                }
            } catch (e: SocketTimeoutException) {
                _toastMessage.value = Result.Error(message = "Не удалось получить ответ от сервера")
            } catch (e: CancellationException) {
                _toastMessage.value = Result.Error(message = e.message.toString())
                throw e
            }
        }
    }
}