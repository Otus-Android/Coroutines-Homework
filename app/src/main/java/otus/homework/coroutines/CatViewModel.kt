package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.SocketTimeoutException

class CatViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatImageService
) : ViewModel()  {

    private var _result = MutableLiveData<Result>()
    val result = _result

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

            } catch (exc: Exception) {
                if (exc is SocketTimeoutException) {
                    _result.value = Error("Не удалось получить ответ от сервера")
                } else {
                    _result.value = Error(exc.message.toString())
                }
            }
        }
    }


    class Factory(
        private val catsService: CatsService,
        private val catsImageService: CatImageService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatViewModel(catsService, catsImageService) as T
        }
    }
}