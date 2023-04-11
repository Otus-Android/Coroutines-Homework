package otus.homework.coroutines

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.*

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private val mutableResultOfRequestFactWithImageLiveData =
        MutableLiveData<Result<FactWithImage>>()
    val factWithImageLiveData: LiveData<Result<FactWithImage>> get() = mutableResultOfRequestFactWithImageLiveData

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning()
            mutableResultOfRequestFactWithImageLiveData.postValue(
                Result.Error(
                    throwable.message,
                    throwable
                )
            )
        }

    fun requestFactAndImage() {
        viewModelScope.launch(coroutineExceptionHandler + SupervisorJob()) {
            val textFact = async(Dispatchers.IO) {
                try {
                    val response = catsService.getCatFact()
                    if (response.isSuccessful && response.body() != null)
                        response.body()!!.fact
                    else ""

                } catch (e: java.net.SocketTimeoutException) {
                    mutableResultOfRequestFactWithImageLiveData
                        .postValue(Result.Error(e.message, e))
                    ""

                } catch (e: java.lang.Exception) {
                    mutableResultOfRequestFactWithImageLiveData
                        .postValue(Result.Error(e.message, e))
                    ""
                }

            }

            val image = async(Dispatchers.IO) {
                try {
                    val response = imageService.getImage()
                    if (response.isSuccessful
                        && response.body() != null
                        && response.body()!!.isNotEmpty()
                    ) {
                        response.body()!![0].url
                    } else ""

                } catch (e: java.net.SocketTimeoutException) {
                    mutableResultOfRequestFactWithImageLiveData
                        .postValue(Result.Error(e.message, e))
                    ""

                } catch (e: java.lang.Exception) {
                    mutableResultOfRequestFactWithImageLiveData
                        .postValue(Result.Error(e.message, e))
                    ""
                }
            }
            val factWithImage = FactWithImage(textFact.await(), image.await())
            if (factWithImage.imageUrl.isNotEmpty()) {
                mutableResultOfRequestFactWithImageLiveData.value = Result.Success(factWithImage)
            }

        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val catsService: CatsService, private val imageService: ImageService) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return CatsViewModel(catsService, imageService) as T
        }
    }
}