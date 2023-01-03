package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.data.CrashMonitor
import otus.homework.coroutines.model.CatImageUrl
import otus.homework.coroutines.model.CatModel
import otus.homework.coroutines.model.Fact
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsViewModel(private val catsService: CatsService) : ViewModel() {

    private val result = MutableSharedFlow<Result>()
    val resultObservable: SharedFlow<Result> = result.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)
    }

    init {
        onInitComplete()
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            try {
                val catModel = coroutineScope {
                    val factAsync = async(Dispatchers.IO) { catsService.getFact() }
                    val imageUrlAsync = async(Dispatchers.IO) { catsService.getImageUrl() }

                    createCatModel(factAsync.await(), imageUrlAsync.await())
                }
                catModel?.let { result.emit(Result.Success(it)) }
            } catch (throwable: Throwable) {
                when (throwable) {
                    is SocketTimeoutException -> result.emit(Result.Error(R.string.error_message_response_failed))
                    else -> {
                        CrashMonitor.trackWarning(throwable)
                        throwable.message?.let { message ->
                            result.emit(Result.Error(message))
                        }
                    }
                }
            }
        }
    }

    private fun createCatModel(
        factResponse: Response<Fact>,
        imageUrlResponse: Response<CatImageUrl>
    ): CatModel? {
        return if (factResponse.isSuccessful && imageUrlResponse.isSuccessful) {
            val title = factResponse.body()?.title
            val imageUrl = imageUrlResponse.body()?.url
            CatModel(title, imageUrl)
        } else null
    }

    class Factory(private val catsService: CatsService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatsViewModel(catsService) as T
        }
    }
}