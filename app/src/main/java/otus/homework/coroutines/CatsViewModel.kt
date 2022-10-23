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

    private val _catData = MutableLiveData<CatData>()
    val catData = _catData.toImmutable()

    private val _socketTimeoutExceptionEvent = SingleLiveEvent<Unit>()
    val socketTimeoutExceptionEvent = _socketTimeoutExceptionEvent.toImmutable()

    private val _defaultExceptionEvent = SingleLiveEvent<String?>()
    val defaultExceptionEvent = _defaultExceptionEvent.toImmutable()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(CatsPresenter::class.simpleName, throwable)
        _defaultExceptionEvent.value = throwable.message
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

            _catData.value = CatData(fact, picUrl)
        }
    }

    private inline fun <T> fetchData(action: () -> T): T? {
        return try {
            action.invoke()
        } catch (ex: SocketTimeoutException) {
            _socketTimeoutExceptionEvent.call()
            null
        }
    }

}

fun <T> MutableLiveData<T>.toImmutable() = this as LiveData<T>