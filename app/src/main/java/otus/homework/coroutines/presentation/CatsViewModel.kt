package otus.homework.coroutines.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    initialState: CatsState = CatsState.default()
) : ViewModel() {

    val state by lazy(LazyThreadSafetyMode.NONE) { _state.asStateFlow() }
    val event by lazy(LazyThreadSafetyMode.NONE) { _events.asSharedFlow() }

    private val _state = MutableStateFlow(initialState)
    private val _events = MutableSharedFlow<ErrorType>(extraBufferCapacity = 1)

    init {
        fetch()
    }

    fun fetch() {
        viewModelScope.launch {
            val getFactDeferred = async { getFact() }
            val getImageDeferred = async { getRandomImage() }

            val getFactResult = getFactDeferred.await()
            val imageResult = getImageDeferred.await()

            handleResults(getFactResult, imageResult)
        }
    }

    private suspend fun getFact() =
        wrapRequest { catsService.getCatFact() }

    private suspend fun getRandomImage() =
        wrapRequest { catsService.getRandomImage() }

    private fun handleResults(
        getFactResult: Result<Fact>,
        imageResult: Result<Image>
    ) {
        val fact = when (getFactResult) {
            is Result.Success -> getFactResult.data
            is Result.Error -> {
                _events.tryEmit(getFactResult.type)
                null
            }
        }

        val image = when (imageResult) {
            is Result.Success -> imageResult.data
            is Result.Error -> {
                _events.tryEmit(imageResult.type)
                null
            }
        }

        val catsViewState = CatsState(
            fact = fact,
            image = image
        )

        _state.value = catsViewState
    }

    private suspend fun <T> wrapRequest(block: suspend () -> T): Result<T> =
        try {
            val result = block()
            Result.Success(result)
        } catch (ex: SocketTimeoutException) {
            Result.Error(ErrorType.ServerConnectionError)
        } catch (ex: Exception) {
            checkCancellationException(ex)
            CrashMonitor.trackWarning()

            ex.message?.let { message ->
                Result.Error(ErrorType.OccurredException(message))
            } ?: Result.Error(ErrorType.UnknownError)
        }

    private fun checkCancellationException(throwable: Throwable) {
        if (throwable is CancellationException) {
            throw throwable
        }
    }

}