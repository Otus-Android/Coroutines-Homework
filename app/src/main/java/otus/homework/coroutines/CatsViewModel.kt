package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService,
) : ViewModel() {

    private val _catInfo = MutableLiveData<Result<CatInfo>>()
    val catInfo: LiveData<Result<CatInfo>> = _catInfo

    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception.message)
    }

    fun onInitComplete() {
        viewModelScope.launch(handler) {
            try {
                val factAsync = async { loadFact() }
                val catImageAsync = async { loadImageUrl() }
                val factText = factAsync.await()?.fact
                val imageUrl = catImageAsync.await()?.url

                _catInfo.postValue(Result.Success(CatInfo(text = factText, imageUrl = imageUrl)))
            } catch (e: SocketTimeoutException) {
                _catInfo.postValue(Result.Error("Не удалось получить ответ от сервером"))
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catInfo.postValue(Result.Error(e.message))
            }
        }
    }

    private suspend fun loadFact(): FactNew? = invokeSafely { catsService.getCatFact() } as FactNew?

    private suspend fun loadImageUrl(): CatImage? =
        invokeSafely { catsImageService.getCatImage() } as CatImage?

    private suspend fun invokeSafely(block: suspend () -> Response<out Any>): Any? {
        val response = block()
        if (response.isSuccessful && response.body() != null) {
            return response.body()
        } else {
            CrashMonitor.trackWarning()
        }
        return null
    }


    class CatModelFactory(
        private val catsService: CatsService,
        private val imageService: CatsImageService,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatsViewModel(catsService, imageService) as T
        }
    }
}

sealed class Result<out T> {
    class Success<out T>(val value: T) : Result<T>()
    class Error(val message: String?) : Result<Nothing>()

}