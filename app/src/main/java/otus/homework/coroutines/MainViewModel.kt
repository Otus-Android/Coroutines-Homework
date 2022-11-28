package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.error.handler.CrashMonitor
import otus.homework.coroutines.error.handler.Result
import otus.homework.coroutines.network.facts.base.AbsCatService
import otus.homework.coroutines.network.facts.base.CatData
import otus.homework.coroutines.network.facts.base.image.ImageService
import java.net.SocketTimeoutException

class MainViewModel(
    private val catsService: AbsCatService,
    private val catsImageService: ImageService
) : ViewModel() {

    private var _catsView: ICatsView? = null

    private val _viewModelScope = viewModelScope +
            CoroutineExceptionHandler { _, t ->
                CrashMonitor.trackWarning(t)
            }

    fun onStart() {
        _viewModelScope.launch {
            val catFactCall = async { catsService.getCatFact() }
            val catImageCall = async { catsImageService.getCatImageUrl() }

            try {
                val catFact = catFactCall.await()
                val catImage = catImageCall.await()
                val catData = CatData(catFact, catImage)
                _catsView?.populate(Result.Success(catData))

            } catch (exp: Exception) {
                Log.d("cats", "Exception is ${exp.message}")
                when (exp) {
                    is CancellationException -> throw exp
                    is SocketTimeoutException ->_catsView?.populate(Result.Error(exp))
                    else -> _catsView?.populate(Result.Error(exp))
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    private fun detachView() {
        _catsView = null
    }

    override fun onCleared() {
        super.onCleared()
        detachView()
    }
}