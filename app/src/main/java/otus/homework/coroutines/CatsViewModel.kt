package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.model.FullCatFact
import otus.homework.coroutines.model.Result
import otus.homework.coroutines.service.CatsService
import otus.homework.coroutines.service.PicturesService
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val picturesService: PicturesService,
) : ViewModel() {

    private val _result = MutableLiveData<Result<FullCatFact>>()
    val result: LiveData<Result<FullCatFact>> = _result

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        val exception = throwable as? Exception ?: RuntimeException(throwable)
        if (exception !is SocketTimeoutException) {
            CrashMonitor.trackWarning(exception)
        }
        _result.value = Result.Error(exception)
    }

    fun getCatFactAndPic() =
        viewModelScope.launch(exceptionHandler) {
            val catFactDeferred = async { catsService.getCatFact() }
            val catPicDeferred = async { picturesService.getPic() }
            val catFact = catFactDeferred.await()
            val catPic = catPicDeferred.await()

            _result.value = Result.Success(
                FullCatFact(fact = catFact, imageUrl = catPic.first().url)
            )
        }
}
