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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

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
                val fact = async { catsService.getCatFact() }

                val image = async { catsImageService.getCatImage().first() }

                val factUI = FactImageUI(
                    text = fact.await().fact,
                    url = image.await().url
                )

                _state.value = Result.Success(factUI)

            } catch (e: SocketTimeoutException) {
                Log.e("XXX", "$e")
                _state.value = Result.Error(e)
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