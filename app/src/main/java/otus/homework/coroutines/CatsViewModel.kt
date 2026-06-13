package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) : ViewModel() {

    private val _presentationState = MutableStateFlow<Result<PresentationModel>?>(null)
    val presentationState = _presentationState.asStateFlow()

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception as Exception)
        _presentationState.value = Result.Error(exception.message ?: "")
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            try {
                val factDeferred = async(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                val imageDeferred = async(Dispatchers.IO) {
                    catsImageService.getCatImage()
                }

                val fact = factDeferred.await()
                val image = imageDeferred.await()

                _presentationState.value = Result.Success(
                    PresentationModel(fact = fact.fact, imageUrl = image[0].url)
                )

            } catch (_: SocketTimeoutException) {
                _presentationState.value = Result.Error(
                    "Не удалось получить ответ от сервера"
                )
            }
        }
    }
}

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            return CatsViewModel(catsService, catsImageService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}