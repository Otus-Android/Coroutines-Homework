package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    var catResult: MutableLiveData<Result<CatResult>> = MutableLiveData()

    init {
        loadData()
    }

    private val errorHandlerException: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            if (exception is SocketTimeoutException) {
                catResult.value = Result.Error
            } else {
                CrashMonitor.trackWarning()
            }
        }

    private fun loadData() {
        viewModelScope.launch(errorHandlerException) {
            val factDeferred = async { catsService.getCatFact() }
            val imageDeferred = async { catsService.getCatImage() }

            val fact = factDeferred.await()
            val image = imageDeferred.await()

            if (fact.isSuccessful && fact.body() != null) {
                throw IllegalStateException("Incorrect fact response: ${fact.message()}")
            }

            if (image.isSuccessful && image.body() != null) {
                throw IllegalStateException("Incorrect image response: ${image.message()}")
            }

            catResult.value = Result.Success(CatResult(fact.body()!!, image.body()!!))
        }
    }

    fun cancel() {
        viewModelScope.cancel()
    }
}
