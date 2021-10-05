package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.model.Cats
import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.model.ImageCat
import otus.homework.coroutines.model.Result
import otus.homework.coroutines.service.CatsService
import otus.homework.coroutines.service.ImageCatsService
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageCatsService: ImageCatsService
) : ViewModel() {

    private var _catsView: ICatsView? = null
    val stateLiveData = MutableLiveData<Result>()

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning(throwable)
            stateLiveData.value = Result.Error(throwable)
        }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            try {
                val fact = async(Dispatchers.IO) { catsService.getCatFact() }
                val imageCat = async(Dispatchers.IO) { imageCatsService.getImageCat() }
                stateLiveData.value = Result.Success((Cats(fact.await().text,imageCat.await().file ?: "")))
            } catch (e: SocketTimeoutException){
                stateLiveData.value = Result.Error(e)
            } catch (e: Exception){
                stateLiveData.value = Result.Error(e)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }
}