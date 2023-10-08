package otus.homework.coroutines

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class CatViewModel(
    private val catsService: CatsService,
    private val imageCatsService: ImageCatsService,
    private val application: Application,
    private val catsMapper: CatsMapper
): ViewModel() {

    companion object {

        private const val SOCKET_TIMEOUT_EXCEPTION = "Не удалось получить ответ от сервера"
        private const val ERROR_MESSAGE = "Произошла ошибка"
    }

    private var _catsView: ICatsView? = null

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(application, exception.message ?: ERROR_MESSAGE)
    }

    fun onInitComplete() {
        viewModelScope.launch(errorHandler) {
            when(val responseResult = onInitCompleteResponse()) {
                is Success -> {
                    _catsView?.populate(responseResult.data)
                }
                is Error -> {
                    CrashMonitor.trackWarning(application, responseResult.message)
                }
            }
        }
    }

    private suspend fun onInitCompleteResponse(): Result<FactAndImageModel> {
        return try {
            val response = catsService.getCatFact()
            val imageResponse = imageCatsService.getCatImage()
            if ((response.isSuccessful && response.body() != null) && (imageResponse.isSuccessful && imageResponse.body() != null)) {
                val factAndImage = catsMapper.toFactAndImage(
                    fact = response.body()?.fact,
                    image = imageResponse.body()?.image
                )
                Success(factAndImage)
            } else {
                Error(SOCKET_TIMEOUT_EXCEPTION)
            }
        } catch (e: java.net.SocketTimeoutException) {
            Error(SOCKET_TIMEOUT_EXCEPTION)
        } catch (e: Exception) {
            Error(e.message ?: ERROR_MESSAGE)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}