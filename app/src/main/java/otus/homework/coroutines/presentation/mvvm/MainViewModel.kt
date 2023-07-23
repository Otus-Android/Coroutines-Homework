package otus.homework.coroutines.presentation.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.domain.CatModel
import otus.homework.coroutines.domain.GetCatModelUseCase
import otus.homework.coroutines.presentation.CrashMonitor
import java.net.SocketTimeoutException

class MainViewModel(private val catUseCase: GetCatModelUseCase) : ViewModel() {

    private val _state = MutableStateFlow<MainState<CatModel?>>(MainState.Success(null))
    val state = _state.asStateFlow()

    init {
        loadCatModel()
    }

    fun loadCatModel() {
        viewModelScope.launch(CoroutineExceptionHandler { _, _ -> CrashMonitor.trackWarning() }) {
            try {
                val catModel = catUseCase.invoke()
                _state.value = MainState.Success(catModel)
            } catch (e: CancellationException) {
                throw e
            } catch (_: SocketTimeoutException) {
                _state.value = MainState.Error
            }
        }
    }
}
