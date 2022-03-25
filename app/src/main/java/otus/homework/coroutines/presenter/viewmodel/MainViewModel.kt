package otus.homework.coroutines.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.domain.Cat
import java.lang.Exception
import otus.homework.coroutines.data.Result
import otus.homework.coroutines.utils.CrashMonitor

const val pause: Long = 8000L;

class MainViewModel(val service: CatsService): ViewModel() {
    private val TAG: String = "ViewModel";
    private val _catData: MutableLiveData<Result<Cat>> = MutableLiveData<Result<Cat>>();
    val catData: LiveData<Result<Cat>> = _catData
    private var dataJob: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler{
        context, exception -> CrashMonitor.trackWarning(exception)
    }
     init {
        Log.i("ViewModel", "viewModel init")
    }
    fun loadData() {
        if(dataJob?.isActive == true) return
        dataJob = viewModelScope.launch(exceptionHandler) {
            try {
                _catData.value = Result.Loading
                Log.i("ViewModel", "dataJob start")
                delay(pause);
                val fact = async { service.getCatFact() }
                val imageUrl = async { service.getImageResource() }
                Log.i("ViewModel", "dataJob success")
                _catData.value = Result.Success(
                    Cat(
                        fact.await().text,
                        imageUrl.await().fileUrl
                    )
                )
                } catch (ex: Exception) {
                    Log.i("ViewModel", "dataJob exception")
                    if (ex is CancellationException) {
                        Log.i(TAG, "Job was cancelled")
                    } else {
                        _catData.value = Result.Error(ex)
                    }
                } finally {
                    withContext(NonCancellable) {
                        delay(1000);
                        Log.i(TAG, "cleaning process")
                    }
                }
        }
    }

    fun cancelLoad() {
        dataJob?.cancel(CancellationException("my errors"))
    }
}