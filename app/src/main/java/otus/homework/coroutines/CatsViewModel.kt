package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CatsViewModel(
    private val serviceCatFact: CatsService,
    private val serviceCatImage: CatsService
): ViewModel() {

    var result: MutableLiveData<CatsResult> = MutableLiveData()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        CrashMonitor.trackWarning(throwable)
        result.postValue(CatsResult.Error(throwable.message.toString()))
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            val catFact = async { serviceCatFact.getCatFact() }
            val catImage = async { serviceCatImage.getCatImage() }
            result.postValue(CatsResult.Success(CatModel(catFact.await().fact, catImage.await().url)))
        }
    }
}