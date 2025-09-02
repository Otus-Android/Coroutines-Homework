package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineExceptionHandler

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) : ViewModel() {


    private val _uiState = MutableLiveData<Result>()

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        CrashMonitor.trackWarning("${e.message}")
        _uiState.value = Result.Error( e )
    }

    val uiState: LiveData<Result>
        get() = _uiState

    fun populate () {
        viewModelScope.launch(exceptionHandler) {
            val fact = async { catsService.getCatFact() }
            val image = async { catsImageService.getCatImage()[0] }
            val cat = Cat(fact.await().fact, image.await().url)

            _uiState.value = Result.Success(cat)
        }
    }

    companion object {
        fun provideFactory(
            catsService: CatsService,
            catsImageService: CatsImageService
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                CatsViewModel(catsService, catsImageService)
            }
        }
    }
}