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
import java.net.SocketTimeoutException

private val TAG = "CatsViewModel"

class CatsViewModel(private val catsService: CatsService) : ViewModel() {
    private val _catDescription = MutableLiveData<Result<CatDescription>?>()
    val catDescription: LiveData<Result<CatDescription>?> get() = _catDescription

    val isLoading: LiveData<Boolean> = Transformations.map(_catDescription) {
        return@map it == Result.Loading
    }

    fun updateCat() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            _catDescription.value = when (throwable) {
                is CancellationException -> {
                    Log.d(TAG, "Can we catch CancellationException?", throwable)
                    return@CoroutineExceptionHandler
                }
                is SocketTimeoutException -> {
                    Log.d(TAG, "Network error occurred", throwable)
                    Result.Error(R.string.error_network)
                }
                else -> {
                    Log.d(TAG, "Unknown error", throwable)
                    Result.Error(throwable)
                }
            }
            CrashMonitor.trackWarning()
        }

        viewModelScope.launch(exceptionHandler) {
            _catDescription.value = Result.Loading
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