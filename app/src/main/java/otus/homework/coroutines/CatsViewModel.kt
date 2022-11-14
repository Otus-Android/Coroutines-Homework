package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.util.logging.Logger

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private var _catResult: MutableLiveData<Result<CatResult>> = MutableLiveData()

    val catResult: LiveData<Result<CatResult>>
     get() = _catResult

    init {
        loadData()
    }

    private val errorHandlerException: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            Logger.getLogger("CatsViewModel")
                .info("CoroutineExceptionHandler got ${exception.message}")
        }

    private fun loadData() {
        viewModelScope.launch(errorHandlerException) {
            try {
                val factDeferred = async { catsService.getCatFact() }
                val imageDeferred = async { catsService.getCatImage() }

                val fact = factDeferred.await()
                val image = imageDeferred.await()

                if (!fact.isSuccessful && fact.body() == null) {
                    throw IllegalStateException("Incorrect fact response: ${fact.message()}")
                }

                if (!image.isSuccessful && image.body() == null) {
                    throw IllegalStateException("Incorrect image response: ${image.message()}")
                }

                _catResult.value = Result.Success(CatResult(fact.body()!!, image.body()!!))
            } catch (exception: Exception) {
                if (exception is SocketTimeoutException) {
                    _catResult.value = Result.Error
                } else {
                    CrashMonitor.trackWarning()
                }
            }
        }
    }
}
