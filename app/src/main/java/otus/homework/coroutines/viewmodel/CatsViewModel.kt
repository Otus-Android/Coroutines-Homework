package otus.homework.coroutines.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.CatsService
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.ImageService
import otus.homework.coroutines.R
import otus.homework.coroutines.model.CatData
import java.net.SocketTimeoutException

/**
 * @author Kovrizhkin V. on 08.11.2021
 */

interface CatsViewModel {
    val catResultState: MutableLiveData<Result>
    fun onInitComplete()
}

class CatsViewModelImpl(
    private val factService: CatsService,
    private val imageService: ImageService
) : ViewModel(), CatsViewModel {

    override val catResultState = MutableLiveData<Result>()

    override fun onInitComplete() {
        val handler = CoroutineExceptionHandler { _, e -> onError(e) }

        viewModelScope.launch(handler + SupervisorJob()) {
            val fact = async(Dispatchers.IO) { factService.getCatFact() }
            val image = async(Dispatchers.IO) { imageService.getCatImage() }

            val result = CatData(image.await().path, fact.await())
            catResultState.value = Result.Success(result)
        }
    }

    private fun onError(e: Throwable) {
        val errorState = when (e) {
            is SocketTimeoutException -> {
                Result.Error(R.string.socket_timeout_error_text)
            }
            else -> {
                CrashMonitor.trackWarning()
                e.message?.let {
                    Result.Error(it)
                } ?: Result.Error(R.string.unknown_error)
            }
        }
        catResultState.value = errorState
    }
}

@SuppressLint("UNCHECKED_CAST")
class CatsViewModelFactory(
    private val factService: CatsService,
    private val imageService: ImageService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatsViewModelImpl(
            factService,
            imageService
        ) as T
    }
}