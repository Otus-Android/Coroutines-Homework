package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) : ViewModel() {
    private val populateStateFlow = MutableStateFlow<Result?>(value = null)
    private val progressDialogStateFlow = MutableStateFlow<Boolean>(false)
    private val toastStateFlow = MutableStateFlow<String?>(value = null)

    fun onInitComplete() {
        viewModelScope.launch(getCoroutineExceptionHandler()) {
            progressDialogStateFlow.emit(true)
            val factDeferred =
                async {
                    try {
                        catsService.getCatFact(FACT_URI)
                    } catch (e: Exception) {
                        when (e) {
                            SocketTimeoutException() ->
                                toastStateFlow.emit("Не удалось получить ответ от сервера")
                            else -> {
                                CrashMonitor.trackWarning()
                                e.message?.let { toastStateFlow.emit(it) }
                            }
                        }
                        throw CancellationException(e.message)
                    }
                }

            val imageUriDeferred =
                async {
                    try {
                        catsImageService.getCatImage(IMAGE_URI)
                    } catch (e: Exception) {
                        when (e) {
                            SocketTimeoutException() ->
                                toastStateFlow.emit("Не удалось получить ответ от сервера")
                            else -> {
                                CrashMonitor.trackWarning()
                                e.message?.let { toastStateFlow.emit(it) }
                            }
                        }
                        throw CancellationException(e.message)
                    }
                }
            populateStateFlow.emit(
                Result.Success(
                    Pair(
                        factDeferred.await(),
                        imageUriDeferred.await()
                    )
                )
            )
            progressDialogStateFlow.emit(false)
        }
    }

    internal fun getPopulateFlow(): Flow<Result?> = populateStateFlow
    internal fun getProgressDialogsFlow(): Flow<Boolean> = progressDialogStateFlow
    internal fun getToastFlow(): Flow<String?> = toastStateFlow

    private fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, _ ->
            CrashMonitor.trackWarning()
        }
    }

    companion object {
        private const val FACT_URI = "https://cat-fact.herokuapp.com/facts/random?animal_type=cat"
        private const val IMAGE_URI = "https://aws.random.cat/meow"
    }
}