package otus.homework.coroutines.presentation

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.data.CatsFactService
import otus.homework.coroutines.data.CatsImageService
import otus.homework.coroutines.util.Result

class CatsViewModel(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService,
) : ViewModel() {
    private val _uiState: MutableLiveData<Result> = MutableLiveData(Result.Loading)
    val uiState: LiveData<Result> = _uiState

    private var catsJob: Job? = null
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning()
        _uiState.value = Result.Error(throwable)
    }

    fun onInitComplete() {
        catsJob =
            viewModelScope.launch(coroutineExceptionHandler + CoroutineName("CatsCoroutine")) {
                val imageDeferred = async { catsImageService.getCatImage().first() }
                val factDeferred = async { catsFactService.getCatFact() }

                val image = imageDeferred.await()
                val fact = factDeferred.await()
                _uiState.value = Result.Success(
                    CatsInfo(
                        fact = fact.fact,
                        imageUrl = image.url,
                    )
                )
            }
    }

    fun catsJobCancel() {
        catsJob?.cancel()
    }

    companion object {
        fun provideFactory(
            catsFactService: CatsFactService,
            catsImageService: CatsImageService,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null,
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return CatsViewModel(catsFactService, catsImageService) as T
                }
            }
    }

}
