package otus.homework.coroutines.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.data.CatDto
import otus.homework.coroutines.data.CatsRepo
import java.net.SocketTimeoutException

class CatsViewModel : ViewModel() {

    val catResultFlow: StateFlow<Result<CatDto>>
        get() = catMutableFlow

    private val catMutableFlow: MutableStateFlow<Result<CatDto>> = MutableStateFlow(Result.Loading)

    private val repo: CatsRepo = DiContainer.catsRepo

    init {
        loadCatFactWithImage()
    }

    fun loadCatFactWithImage() {
        catMutableFlow.tryEmit(Result.Loading)

        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackException(
                throwable
            )
        }) {
            try {
                catMutableFlow.tryEmit(Result.Success(repo.getCatsFactsWithPhoto()))
            } catch (e: Exception) {
                val error = when (e) {
                    is SocketTimeoutException -> CatsError.NetworkError
                    else -> CatsError.DefaultError(e.message)
                }
                catMutableFlow.tryEmit(Result.Error(error))
            }
        }
    }
}