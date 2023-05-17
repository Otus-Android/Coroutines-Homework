package otus.homework.coroutines

import android.annotation.SuppressLint
import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException


class CatViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatImageService
) : ViewModel()  {

    private var _result: MutableLiveData<Result> = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    private val handler = CoroutineExceptionHandler { _, exc ->
        val msg = exc.message.toString()
        CrashMonitor.trackWarning(msg)
    }

    fun getCatData() {
        viewModelScope.launch(handler) {
            try {
                val fact = async { catsService.getCatFact().fact }
                val url = async { catsImageService.getImage()[0].url }
                _result.value = Success(CatData(
                        url = url.await(),
                        fact = fact.await())
                )
            } catch (exc: SocketTimeoutException) {
                _result.value = Error("Не удалось получить ответ от сервера")
            } catch (exc: CancellationException) {
                throw exc
            }
        }
    }


    class Factory(
        private val catsService: CatsService,
        private val catsImageService: CatImageService
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatViewModel(catsService, catsImageService) as T
        }
    }
}