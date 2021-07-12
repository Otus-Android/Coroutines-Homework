package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import otus.homework.coroutines.domain.AnimalCard
import otus.homework.coroutines.network.Fact

class CatsPresenter(
    private val catsService: CatsService,
    private val imagesService: ImagesService
) {

    private var _catsView: ICatsView? = null

    private val job = SupervisorJob()
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    private val _getFactState = MutableLiveData<Result?>()
    val getFactState: LiveData<Result?>
        get() = _getFactState

    fun onInitComplete() {

        presenterScope.launch {
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
                    _catsView?.populate(animalCard)
                } else {
                    CrashMonitor.trackWarning(factResponse.errorBody()?.string())
                }
            } catch (ex: Exception) {
                _getFactState.value = Error(ex)
                CrashMonitor.trackWarning(ex.message.toString())
            }
        }
    }

    fun onMessageShown() {
        _getFactState.value = null
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job.cancel()
    }
}

sealed class Result
data class Error(val t: Throwable?) : Result()
object Empty : Result()
data class Success(val data: Fact) : Result()