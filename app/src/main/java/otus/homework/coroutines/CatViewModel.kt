package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.model.CatModel
import java.net.SocketTimeoutException


class CatViewModel(
    private val factService: CatsFactService,
    private val imageService: CatsImageService
) : ViewModel() {
    val catModel = MutableLiveData<Result>()

    class CatsViewModelFactory(private val factService: CatsFactService,
                               private val imageService: CatsImageService) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatViewModel(factService, imageService) as T
        }
    }

    fun onInitComplete() {
        loadData()
    }

    private val handler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is SocketTimeoutException -> {
                catModel.value = Result.Error(Throwable("Не удалось получить ответ от сервера"))
            }
            is CancellationException -> {
                throw throwable
            }
            else -> {
                CrashMonitor.trackWarning(throwable.message.toString())
                catModel.value = Result.Error(throwable)
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch(handler) {
            val catFactJob = async { factService.getCatFact() }
            val catImageJob = async { imageService.getCatImage() }

            catModel.value = Result.Success(
                CatModel(
                    catFactJob.await().body()?.fact,
                    CatsImageService.BASE_URL + catImageJob.await().body()?.url
                )
            )

        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
