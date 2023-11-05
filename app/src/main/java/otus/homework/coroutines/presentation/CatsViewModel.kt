package otus.homework.coroutines.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import otus.homework.coroutines.R
import otus.homework.coroutines.data.Result
import otus.homework.coroutines.domain.repository.FactRepository
import otus.homework.coroutines.domain.repository.ImageUrlRepository
import otus.homework.coroutines.presentation.utlis.MessageProvider
import javax.inject.Inject
import javax.inject.Provider

class CatsViewModel @Inject constructor(
    private val factRepository: FactRepository,
    private val imageUrlRepository: ImageUrlRepository,
    private val messageProvider: Provider<MessageProvider>,
) : ViewModel() {

    private val _state = MutableLiveData<ScreenState>(ScreenState.Empty)
    val state: LiveData<ScreenState> get() = _state

    private var factRefreshJob: Job? = null

    private val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _state.value = ScreenState.Error(exception.message)
        }
        CrashMonitor.trackWarning(exception)
    }

    init {
        refreshFact()
    }

    fun refreshFact() {
        _state.value = ScreenState.Loading
        factRefreshJob?.cancel()
        factRefreshJob = viewModelScope.launch(
            exceptionHandler + CoroutineName(COROUTINE_NAME)
        ) {
            val factDeferred = async(Dispatchers.IO) { factRepository.getCatFact() }
            val imageUrlDeferred = async(Dispatchers.IO) { imageUrlRepository.getImageUrl() }

            if (isActive) {
                val factResult = factDeferred.await()
                val urlResult = imageUrlDeferred.await()

                withContext(Dispatchers.Main.immediate) {
                    if (factResult is Result.Success && urlResult is Result.Success) {
                        _state.value = ScreenState.Model(
                            text = factResult.model.text,
                            photoUrl = urlResult.model.url,
                        )
                    } else {
                        val message = messageProvider.get().invoke(R.string.timeout_exception)
                        _state.value = ScreenState.Error(message)
                    }
                }
            }
        }
    }

    fun stopWorking() {
        _state.value = ScreenState.Empty
        viewModelScope.coroutineContext.cancelChildren()
    }

    companion object {
        private const val COROUTINE_NAME = "CatsCoroutine"
    }
}