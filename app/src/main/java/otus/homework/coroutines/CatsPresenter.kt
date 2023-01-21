package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PresenterScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + CoroutineName("CatsCoroutine")
}

class CatsPresenter(
    private val catsService: CatsService
) {

    private val catsData: MutableLiveData<Result<CatsData>> = MutableLiveData()
    val _catsData: LiveData<Result<CatsData>> = catsData
    var getCatFact: Job? = null
        private set

    fun onInitComplete() {
        val scope = PresenterScope()
        getCatFact = scope.launch {
            supervisorScope {
                val fact = async { catsService.getCatFact() }
                val image = async { catsService.getImage() }
                try {
                    catsData.value = Result.Success(CatsData(fact.await().text, image.await().file))
                } catch (e: Throwable) {
                    catsData.value = Result.Error(e)
                }
            }
        }
    }

}