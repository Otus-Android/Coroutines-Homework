package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catPicturesService: CatPicturesService
) : ViewModel() {

    private val _catData = MutableLiveData<Result>()
    val catData: LiveData<Result> = _catData

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(CatsPresenter::class.simpleName, throwable)
        _catData.value = Result.Error(throwable, (_catData.value as? Result.Success)?.data)
    }

    init {
        updateData()
    }

    fun updateData() {
        val factDef = viewModelScope.async { catsService.getCatFact() }
        val picDef = viewModelScope.async { catPicturesService.getCatPicture() }

        viewModelScope.launch(coroutineExceptionHandler) {
            val fact = fetchData { factDef.await().text }
            val picUrl = fetchData { picDef.await().picUrl }

            _catData.value = Result.Success(CatData(fact, picUrl))
        }
    }

    private inline fun <T> fetchData(action: () -> T): T? {
        return try {
            action.invoke()
        } catch (ex: SocketTimeoutException) {
            _catData.value = Result.Error(ex, (_catData.value as? Result.Success)?.data)
            null
        }
    }

}

sealed class Result {
    data class Success(val data: CatData) : Result()
    data class Error(val throwable: Throwable?, val oldData: CatData?) : Result()
}