package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class CatViewModel(
    private val catsService: CatsService,
    private val imagesService: ImagesService
): ViewModel() {
    sealed class Result {
        data class Success(val model: Model): Result()
        data class Error(val e: Exception): Result()
    }

    private val _catsLiveData = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catsLiveData

    fun updateData() {
        viewModelScope.launch(
            CoroutineExceptionHandler { ctx, throwable ->
                CrashMonitor.trackWarning()
            })
        {
            try {
                val fact = async { catsService.getCatFact() }
                val imageData = async { imagesService.getImage() }

                _catsLiveData.value = Result.Success(
                    Model(
                        fact.await().fact,
                        imageData.await()[0].url
                    )
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsLiveData.value = Result.Error(e)
            }
        }
    }

    class CatViewModelFactory(
        private val catsService: CatsService,
        private val imagesService: ImagesService
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CatViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CatViewModel(catsService, imagesService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}