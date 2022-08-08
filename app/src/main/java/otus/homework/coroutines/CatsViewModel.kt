package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.net.SocketTimeoutException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val photoService: PhotoService
) : ViewModel() {

    private val _catsInfo: MutableSharedFlow<Result<CatsData>> = MutableSharedFlow()
    val catsInfo: Flow<Result<CatsData>> get() = _catsInfo

    fun onInitComplete() = viewModelScope.launch(handler) {
        try {
            val factAsync = async(Dispatchers.IO) {
                catsService.getCatFact()
            }
            val photoAsync = async(Dispatchers.IO) {
                photoService.getPhoto()
            }
            unionPopulate(factAsync.await(), photoAsync.await())
        } catch (ex: Exception) {
            _catsInfo.emit(Result.Error(ex))
        }
    }

    private suspend fun unionPopulate(fact: Fact?, photo: Photo?) {
        fact?.let { fact ->
            photo?.let { photo ->
                _catsInfo.emit(Result.Success(CatsData(fact, photo)))
            }
        }
    }


    private val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
        when (exception) {
            is SocketTimeoutException -> {
                viewModelScope.launch {
                    _catsInfo.emit(Result.Error(exception))
                }
            }
            else -> {
                CrashMonitor.trackWarning()
            }
        }
    }
}

