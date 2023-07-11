package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel : ViewModel() {

    val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception)
    }

    private val catsService: CatsService
    private val imgService: ImgService

    private val _factFlow = MutableSharedFlow<Result<UiFactModel>>()
    val factFlow: SharedFlow<Result<UiFactModel>> = _factFlow

    private var catsDeferred: Deferred<String>? = null
    private var imgDeferred: Deferred<String>? = null

    init {
        val di = DiContainer()
        catsService = di.catsService
        imgService = di.imgService
    }

    fun onInitComplete() {

        viewModelScope.launch(handler) {
            try {
                catsDeferred?.cancel()
                imgDeferred?.cancel()

                catsDeferred = viewModelScope.async {
                    catsService.getCatFact().fact
                }
                catsDeferred!!.start()

                imgDeferred = viewModelScope.async {
                    imgService.getRandomImg().message
                }
                imgDeferred!!.start()

                _factFlow.emit(Result.Success(UiFactModel(catsDeferred!!.await(), imgDeferred!!.await())))

            } catch (e: SocketTimeoutException) {
                _factFlow.emit(Result.Error("Не удалось получить ответ от сервером"))
                throw e
            }
        }
    }

}
