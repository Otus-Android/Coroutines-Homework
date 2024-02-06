package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) : ViewModel() {

    private val _state = MutableLiveData<Result<FactImageUI>>()
    val state: LiveData<Result<FactImageUI>> get() = _state

    fun loadFact() {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e("XXX", "$exception")
            _state.value = Result.Error(exception)
            CrashMonitor.trackWarning()
        }

        viewModelScope.launch(handler) {
            try {
                val fact = catsService.getCatFact()

                val image = catsImageService.getCatImage().first()

                _state.value = Result.Success(
                    FactImageUI(
                        text = fact.fact,
                        url = image.url
                    )
                )

            } catch (e1: java.net.SocketTimeoutException) {
                Log.e("XXX", "$e1")
                _state.value = Result.Error(e1)
            }
        }
    }

    companion object {
        fun getViewModelFactory(
            catsService: CatsService,
            catsImageService: CatsImageService
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                CatsViewModel(
                    catsService,
                    catsImageService,
                )
            }
        }
    }
}