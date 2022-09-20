package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
            _catsInfo.emit(Result.Progress)
            coroutineScope {
                val list = awaitAll(
                    async { catsService.getCatFact() },
                    async { photoService.getPhoto() }
                )
                unionPopulate(list.first() as? Fact, list.last() as? Photo)
            }
        } catch (e: SocketTimeoutException) {
            _catsInfo.emit(Result.Error(e))
        }
    }

    private suspend fun unionPopulate(fact: Fact?, photo: Photo?) = fact?.let { localFact ->
        photo?.let { localPhoto ->
            _catsInfo.emit(Result.Success(CatsData(localFact, localPhoto)))
        }
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
        CrashMonitor.trackWarning()
    }
}

