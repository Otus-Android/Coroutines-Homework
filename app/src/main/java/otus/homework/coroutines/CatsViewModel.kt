package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.api_services.CatsService
import otus.homework.coroutines.api_services.ImageCatsService
import otus.homework.coroutines.data.Result

class CatsViewModel(
    private val catsService: CatsService,
    private val imageCatsService: ImageCatsService
) : ViewModel() {

    private val _resultLoading = MutableLiveData<Result>()
    val resultLoading: LiveData<Result>
        get() = _resultLoading

    fun onInitComplete() {
        viewModelScope.launch {
            try {
                val deferredFact = async {
                    catsService.getCatFact()
                }
                val deferredImage = async {
                    imageCatsService.getCatImage()
                }
                withContext(Dispatchers.Main) {
                    _resultLoading.postValue(
                        Result.Success(
                            deferredFact.await().fact,
                            deferredImage.await().file
                        )
                    )
                }
            } catch (e: Throwable) {
                if (e is CancellationException) throw e else {
                    CrashMonitor.trackWarning(CrashMonitor.KEY_LOADING, e)
                    withContext(Dispatchers.Main) {
                        _resultLoading.postValue(Result.Error(e))
                    }
                }
            }
        }
    }
}
