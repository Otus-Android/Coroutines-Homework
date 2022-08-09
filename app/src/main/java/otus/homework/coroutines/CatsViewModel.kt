package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.api.CatsFactService
import otus.homework.coroutines.api.CatsPhotoService
import otus.homework.coroutines.models.Cat
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsFactService: CatsFactService,
    private val catsPhotoService: CatsPhotoService
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        when (e) {
            is CancellationException -> throw e
            else -> {
                if (e !is SocketTimeoutException) {
                    CrashMonitor.trackWarning(e)
                }
                _cats.value = Result.Error(e)
            }
        }
    }

    private val _cats: MutableLiveData<Result<Cat>> = MutableLiveData()
    val cats: LiveData<Result<Cat>>
        get() = _cats

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(exceptionHandler) {
            val factDeferred = async { catsFactService.getCatFact() }
            val photoDeferred = async { catsPhotoService.getCatPhoto() }
            val cat = Cat(factDeferred.await(), photoDeferred.await())
            _cats.value = Result.Success(cat)
        }
    }

}