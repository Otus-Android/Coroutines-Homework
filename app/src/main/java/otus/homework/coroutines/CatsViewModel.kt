package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.net.SocketTimeoutException

private val TAG = "CatsViewModel"

class CatsViewModel(private val catsService: CatsService) : ViewModel() {
    private val _catDescription = MutableLiveData<Result<CatDescription>?>()
    val catDescription: LiveData<Result<CatDescription>?> get() = _catDescription

    fun updateCat() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.d(TAG, "Just logging exception in CoroutineExceptionHandler", throwable)
            CrashMonitor.trackWarning()
        }

        viewModelScope.launch(exceptionHandler) {
            _catDescription.value = Result.Loading
            supervisorScope {
                try {
                    val factDeferred: Deferred<Fact> = async {
                        if (Constants.RESERVE_CATS_SERVER) catsService.getCatFactReserve()
                            .toFact() else catsService.getCatFact()
                    }
                    val imageUrlDeferred = async { catsService.getRandomImage() }
                    _catDescription.value = Result.Success(
                        CatDescription(
                            factDeferred.await().text,
                            imageUrlDeferred.await().file
                        )
                    )
                } catch (exception: Exception) {
                    _catDescription.value = when (exception) {
                        is CancellationException -> throw exception
                        is SocketTimeoutException -> Result.Error(R.string.error_network)
                        else -> Result.Error(exception)
                    }

                    throw exception // отправить исключение в CoroutineExceptionHandler согласно пункту 2 задания "Реализовать решение ViewModel"
                }
            }
        }
    }
}

class CatsViewModelFactory(private val catsService: CatsService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            throw IllegalAccessException("Unknown ViewModel class: " + modelClass.simpleName)
        }
        @Suppress("UNCHECKED_CAST")
        return CatsViewModel(catsService) as T
    }
}