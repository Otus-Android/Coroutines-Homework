package otus.homework.coroutines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.model.CrashMonitor
import otus.homework.coroutines.model.entity.Fact
import otus.homework.coroutines.model.entity.Result
import otus.homework.coroutines.model.entity.Result.Error
import otus.homework.coroutines.model.entity.Result.Success
import otus.homework.coroutines.model.usecase.CatsUseCase
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsUseCase: CatsUseCase
) : ViewModel() {

    private val mutableFactResultFlow = MutableSharedFlow<Result<Fact>>(
        replay = 0,
        extraBufferCapacity = 10
    )

    val factResultFlow = mutableFactResultFlow.asSharedFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning()

        if (throwable is SocketTimeoutException) {
            mutableFactResultFlow.tryEmit(Error)
        }
    }

    fun onCatFactRequestAction() {
        viewModelScope.launch(coroutineExceptionHandler) {
            mutableFactResultFlow.tryEmit(Success(catsUseCase.getCatFact()))
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val catsUseCase: CatsUseCase
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatsViewModel(catsUseCase) as T
        }
    }
}