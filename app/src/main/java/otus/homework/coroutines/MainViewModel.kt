package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import kotlin.Exception

class MainViewModel (
    private val catsService: CatsService,
    private val imageService: ImageService
): ViewModel() {

    private val _state = MutableStateFlow<Result>(Result.Loading())
    val state = _state.asStateFlow()

    fun refresh() {
        viewModelScope.launch(
            CoroutineName("CatsCoroutine")
                    + catsCoroutineExceptionHandler
        ) {
            _state.value = Result.Loading()
            val catFact = async{catsService.getCatFact()}
            val imageUrl = async{imageService.getCatImage()}
            _state.value = Result.Success(
                catFact = testResponse(catFact.await()).body()!!.text,
                imagePath = testResponse(imageUrl.await()).body()!![0].url
            )
        }
    }

    private val catsCoroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        val errValue = "Ошибка: ${exception.message}"
        CrashMonitor.trackWarning(errValue)
        _state.value = Result.Error(err = errValue)
    }

    private fun <T> testResponse(response: Response<T>): Response<T> {
        if (!response.isSuccessful || response.body() == null) {
            throw Exception("incorrect response")
        }
        return response
    }
}