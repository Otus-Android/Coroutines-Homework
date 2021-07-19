package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.domain.AnimalCard
import otus.homework.coroutines.network.AnimalImage
import otus.homework.coroutines.network.Fact

class CatsViewModel(
    private val catsService: CatsService,
    private val imagesService: ImagesService
) : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("TAG", throwable.message.toString())
        CrashMonitor.trackWarning(throwable.message.toString())
    }

    private val _getFactState = MutableLiveData<Result>()
    val getFactState: LiveData<Result>
        get() = _getFactState

    fun onInitComplete() =
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                val factResponseDeferred = async(Dispatchers.IO) { catsService.getCatFact() }
                val imageResponseDeferred = async(Dispatchers.IO) { imagesService.getRandomImage() }

                val factResponse = factResponseDeferred.await()
                val imageResponse = imageResponseDeferred.await()

                if (factResponse.isSuccessful && !factResponse.body().isNullOrEmpty()) {
                    notifyView(factResponse.body()!!, imageResponse.body())
                } else {
                    CrashMonitor.trackWarning(factResponse.errorBody()?.string())
                }
            } catch (ex: Exception) {
                _getFactState.value = Error(ex)
                CrashMonitor.trackWarning(ex.message.toString())
            }
        }

    private fun notifyView(facts: List<Fact>, animalImage: AnimalImage?) {
        val animalCard = AnimalCard(facts.first().fact)
        animalImage?.let {
            animalCard.imageUrl = it.file
        }
        _getFactState.value = Success(animalCard)
    }

    fun onMessageShown() {
        _getFactState.value = Empty
    }

}


