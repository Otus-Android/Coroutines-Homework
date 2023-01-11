package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import otus.homework.coroutines.api_services.CatsService
import otus.homework.coroutines.api_services.ImageCatsService
import otus.homework.coroutines.data.Cat
import otus.homework.coroutines.data.Result

class CatsPresenter(
    private val catsService: CatsService,
    private val imageCatsService: ImageCatsService
) {

    private var _catsView: ICatsView? = null
    private val scope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.IO
                + CoroutineName("CatsCoroutine")
                + CoroutineExceptionHandler { _, t ->
                CrashMonitor.trackWarning(
                CrashMonitor.KEY_LOADING,
                t
            )
        }
    )
    private var job: Job? = null
    private val _resultLoading = MutableLiveData<Result>()
    val resultLoading: LiveData<Result>
        get() = _resultLoading

    fun onInitComplete() {
        job = scope.launch {
            try {
                val deferredFact = async {
                    catsService.getCatFact()
                }
                val deferredImage = async {
                    imageCatsService.getCatImage()
                }
                withContext(Dispatchers.Main) {
                    _catsView?.populate(Cat(deferredFact.await().fact, deferredImage.await().file))
                }
            } catch (e: Throwable) {
                if (e is CancellationException) throw e else {
                    CrashMonitor.trackWarning(CrashMonitor.KEY_LOADING, e)
                    withContext(Dispatchers.Main) {
                        _resultLoading.postValue(Result.Error(e))
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job = null
        _catsView = null
    }
}