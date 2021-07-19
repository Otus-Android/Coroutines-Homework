package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import otus.homework.coroutines.domain.AnimalCard
import otus.homework.coroutines.network.AnimalImage
import otus.homework.coroutines.network.Fact

class CatsPresenter(
    private val catsService: CatsService,
    private val imagesService: ImagesService
) {

    private var _catsView: ICatsView? = null

    private val job = SupervisorJob()
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine") + job)

    fun onInitComplete() {

        presenterScope.launch {
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
                _catsView?.showError(ex.message.toString())
                CrashMonitor.trackWarning(ex.message.toString())
            }
        }
    }

    private fun notifyView(facts: List<Fact>, animalImage: AnimalImage?) {
        val animalCard = AnimalCard(facts.first().fact)
        animalImage?.let {
            animalCard.imageUrl = it.file
        }
        _catsView?.populate(animalCard)
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
data class Success(val data: AnimalCard) : Result()