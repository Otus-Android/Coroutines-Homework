package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import kotlin.Exception

class MainViewModel (
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val mainDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow<Result>(Result.Loading())
    val state = _state.asStateFlow()

    fun refresh() {
        viewModelScope.launch(
            mainDispatcher
                    + CoroutineName("CatsCoroutine")
                    + catsCoroutineExceptionHandler
        ) {
            _state.value = Result.Loading()
            try {
                _state.value = Result.Success(
                    catFact = updateFact(),
                    imagePath = updateImage()
                )
            } catch (error: java.net.SocketTimeoutException) { // add java.net.UnknownHostException ???
                _state.value = Result.Error(err = "Не удалось получить ответ от сервером")
            } catch (error: Exception) {
                CrashMonitor.trackWarning(error.message)
                _state.value = Result.Error(err = "Ошибка: ${error.message}")
            }
        }
    }

    private val catsCoroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        val errValue = "CoroutineExceptionHandler Ошибка: ${exception.message}"
        CrashMonitor.trackWarning(errValue)
        _state.value = Result.Error(err = errValue)
    }

    private suspend fun updateFact(): String {
        val response: Response<Fact> = withContext(ioDispatcher) {
            return@withContext catsService.getCatFact()
        }
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.text
        }
        throw Exception("incorrect fact response")
    }

    private suspend fun updateImage(): String {
        val response: Response<ArrayList<Image>> = withContext(ioDispatcher) {
            return@withContext imageService.getCatImage()
        }
        if (response.isSuccessful && response.body() != null) {
            return response.body()!![0].url
        }
        throw Exception("incorrect image url response")
    }
}