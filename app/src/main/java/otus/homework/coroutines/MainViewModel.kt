package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.welcometothemooncompanion.util.SingleLiveEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.net.SocketTimeoutException

class MainViewModel(
    private val catsImageService: CatsImageService,
    private val catFactService: CatsService
) : ViewModel() {

    val uiState = MutableStateFlow<CatInfo?>(null)
    val uiEvents = SingleLiveEvent<RealString>()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        CrashMonitor.trackWarning()
    }

    init {
        load()
    }

    fun load() {
        viewModelScope.launch(exceptionHandler) {
            runCatching {
                coroutineScope {
                    val fact = async { catFactService.getCatFact() }
                    val imgUrl = async { catsImageService.getCatImage() }
                    CatInfo(fact.await().text, imgUrl.await().url)
                }
            }.onSuccess {
                uiState.value = it
            }.onFailure {
                when (it) {
                    is CancellationException -> throw it
                    is SocketTimeoutException -> {
                        uiEvents.event =
                            RealString.Res(R.string.socket_timeout_exception_toast_message)
                        throw it
                    }
                    else -> uiEvents.event = RealString.Str(it.message.orEmpty())
                }
            }
        }
    }

    companion object {
        fun factory(
            catsImageService: CatsImageService,
            catFactService: CatsService
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(catsImageService, catFactService)
            }
        }
    }
}
