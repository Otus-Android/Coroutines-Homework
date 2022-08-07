package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.models.Cat
import java.net.SocketTimeoutException

class CatsViewModel(
    private val factService: FactService,
    private val imageService: ImageService
) : ViewModel() {

    private val _result = MutableLiveData<Result<Cat>>()
    val result: LiveData<Result<Cat>> = _result

    init {
        onInitComplete()
    }

    fun onInitComplete() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning(
                throwable.message.toString()
            )
        }) {
            try {
                val fact = async { factService.getCatFact() }
                val image = async { imageService.getCatImage() }
                val catData = Cat(fact.await(), image.await())
                _result.value = Success(catData)
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> _result.value =
                        Error("Не удалось получить ответ от сервера")
                    is CancellationException -> CrashMonitor.trackWarning(e.message.toString())
                }
            }
        }
    }
}

class CatsViewModelFactory(
    private val catFactsService: FactService,
    private val catImagesService: ImageService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatsViewModel(catFactsService, catImagesService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}