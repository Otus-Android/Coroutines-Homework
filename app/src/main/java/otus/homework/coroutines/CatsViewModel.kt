package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import java.lang.Error
import java.net.SocketTimeoutException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private val mutableResultOfRequestFactWithImageLiveData =
        MutableLiveData<Result<FactWithImage>>()
    val factWithImageLiveData: LiveData<Result<FactWithImage>> get() = mutableResultOfRequestFactWithImageLiveData

    companion object {

        fun factory(
            catsService: CatsService,
            catsImagesService: ImageService
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CatsViewModel(catsService, catsImagesService) as T
            }
        }
    }

    init {
        requestFactAndImage()
    }

    fun requestFactAndImage() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            mutableResultOfRequestFactWithImageLiveData.value =
                Result.Error(Error(throwable.message))
            CrashMonitor.trackWarning()
        }) {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { imageService.getImage().first() }

                val factWithImage = FactWithImage(
                    fact.await().fact,
                    image.await().url
                )
                mutableResultOfRequestFactWithImageLiveData.value = Result.Success(factWithImage)

            } catch (e: SocketTimeoutException) {
                mutableResultOfRequestFactWithImageLiveData.value =
                    Result.Error(Error("Не удалось получить ответ от сервера"))
            }
        }
    }
}

