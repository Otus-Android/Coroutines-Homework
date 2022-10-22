package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) : ViewModel() {

    private val _uiState = MutableStateFlow<Result<Fact>>(Result.Loading("loading..."))
    val uiState: StateFlow<Result<Fact>> = _uiState

    //private var _catsView: ICatsView? = null
    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception.toString())
        _uiState.value = Result.Error(exception.toString())
    }

    private suspend fun getCatFact(): Flow<TextFact> = flow { emit(catsService.getCatFact()) }
    private suspend fun getCatImage(): Flow<ImageFact> =
        flow { emit(catsImageService.getCatImage()) }

    fun getCatFactsAndImage() {
        viewModelScope.launch(handler) {
            try {
                getCatFact()
                    .combine(getCatImage()) { textFact, imageFact ->
                        Fact(text = textFact.text, file = imageFact.file)
                    }
                    .flowOn(Dispatchers.IO)
                    .collect { fact ->
                        _uiState.value = Result.Success(fact)
                    }
            } catch (e: java.net.SocketTimeoutException) {
                _uiState.value = Result.Error("Не удалось получить ответ от сервера")
            }
            /*catch (e: retrofit2.HttpException) {
                _uiState.value = Result.Error("404")
            }*/
        }
    }

    init {
        getCatFactsAndImage()
    }

}