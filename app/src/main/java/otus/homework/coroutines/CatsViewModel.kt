package otus.homework.coroutines

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val resources: Resources,
) : ViewModel() {

    private val _catsFact = MutableStateFlow<Result>(Result.Success(Unit))
    val catsFact = _catsFact.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        CrashMonitor.trackWarning()
    }

    fun load() {
        viewModelScope.launch(exceptionHandler) {
            runCatching {
                val facts = catsService.getCatFact()
                val imageUrl = catsService.loadImage().first().url

                CatFact(
                    fact = facts,
                    imageUrl = imageUrl,
                )
            }.onFailure { error ->
                if (error is CancellationException) throw error
                _catsFact.update {
                    Result.Error(errorHandler(error))
                }
            }.onSuccess { fact ->
                _catsFact.update {
                    Result.Success(fact)
                }
            }
        }
    }

    private fun errorHandler(e: Throwable): String {
        Log.d("Fact", e.message.toString())
        return if (e is SocketTimeoutException) {
            resources.getString(
                R.string.socet_timeout_error,
            )
        } else {
            e.message.orEmpty()
        }
    }
}


sealed interface Result {
    data class Success<T>(val result: T) : Result
    data class Error(val message: String) : Result
}