package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
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
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { catsService.getImage() }
                catsData.value = Result.Success(CatsData(fact.await().text, image.await().file))
            } catch (e: Exception) {
                catsData.value = Result.Error(e)
            }
        }
    }

}