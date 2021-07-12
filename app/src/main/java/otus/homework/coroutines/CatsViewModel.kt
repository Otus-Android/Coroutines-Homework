package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.domain.AnimalCard

class CatsViewModel(
    private val catsService: CatsService,
    private val imagesService: ImagesService
) : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("TAG", throwable.message.toString())
        CrashMonitor.trackWarning(throwable.message.toString())
    }

    private val _getFactState = MutableLiveData<Result?>()
    val getFactState: LiveData<Result?>
        get() = _getFactState

    fun onInitComplete() =
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                val factResponseDeferred = async { catsService.getCatFact() }
                val imageResponseDeferred = async { imagesService.getRandomImage() }

                val factResponse = factResponseDeferred.await()
                val imageResponse = imageResponseDeferred.await()

                if (factResponse.isSuccessful && factResponse.body() != null) {
                    val animalCard = AnimalCard(factResponse.body()!!.first().fact)
                    if (imageResponse.isSuccessful && imageResponse.body() != null) {
                        animalCard.imageUrl = imageResponse.body()!!.file
                    }
                    _getFactState.value = Success(animalCard)
                } else {
                    CrashMonitor.trackWarning(factResponse.errorBody()?.string())
                    _getFactState.value = Empty
                }
            } catch (ex: Exception) {
                _getFactState.value = Error(ex)
                CrashMonitor.trackWarning(ex.message.toString())
            }
        }

    fun onMessageShown() {
        _getFactState.value = null
    }

}


