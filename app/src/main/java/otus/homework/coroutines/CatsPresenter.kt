package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService, private val catsServicePicture: CatsServicePicture
) {

    private var _catsView: ICatsView? = null
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main+CoroutineName("CatsCoroutine"))
    private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        CrashMonitor.trackWarning()
        _factAndPicture?.value = Result.Error("Не удалось получить ответ от сервером")
    }
    private val _factAndPicture = MutableLiveData<Result<FactAndPicture>>()
    val factAndPicture: LiveData<Result<FactAndPicture>>
        get() = _factAndPicture

    fun onInitComplete() {

        try {
            scope.launch (){
                val jobFact: Deferred<Fact> = async {
                    catsService.getCatFact()!!}
                val jobUrlPicture: Deferred<List<UrlPicture>> = async {
                    catsServicePicture.getCatPictureUrl()!!}

                _factAndPicture?.value = Result.Success(FactAndPicture(jobFact.await().fact, jobUrlPicture.await()[0].url))
            }
        }catch(e: SocketTimeoutException){
            _factAndPicture?.value = Result.Error("Не удалось получить ответ от сервером")
            CrashMonitor.trackWarning()
        }

    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        scope.cancel()
    }
}