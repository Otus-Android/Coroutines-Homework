package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.api.CatsFactService
import otus.homework.coroutines.api.CatsPhotoService
import otus.homework.coroutines.models.Cat
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsFactService: CatsFactService,
    private val catsPhotoService: CatsPhotoService
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)
    }


    private val _cats: MutableLiveData<Result<Cat>> = MutableLiveData()
    val cats: LiveData<Result<Cat>>
        get() = _cats

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(exceptionHandler) {
            supervisorScope {
                val factDeferred = async { catsFactService.getCatFact() }
                val photoDeferred = async { catsPhotoService.getCatPhoto() }
                try {
                    val cat = Cat(factDeferred.await(), photoDeferred.await())
                    _cats.value = Result.Success(cat)
                } catch (e: Exception) {
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
            }
        }
    }

}