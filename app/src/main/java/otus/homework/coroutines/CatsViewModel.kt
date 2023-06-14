package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsRepository: CatsRepository
) : ViewModel() {
    private var _catsView: ICatsView? = null
    private var job: Job? = null
    private var result: Result? = null

    fun onInitComplete() {
        job = viewModelScope.launch(
            CoroutineExceptionHandler { coroutineContext, throwable ->
                updateResult(Result.Error(throwable))
            }
        ) {
            coroutineScope {
                val fact = async { catsRepository.getCatFact() }
                val imageLink = async { catsRepository.getImageLink() }

                updateResult(Result.Success(Cat(fact.await(), imageLink.await())))
            }
        }
    }

    private fun updateResult(result: Result) {
        this.result = result
        handleResult()
    }

    private fun handleResult() =
        result?.let { result ->
            when (result) {
                is Result.Error ->
                    result.exceptionMessage?.let {
                        _catsView?.showError(result.exceptionMessage)
                    }
                is Result.Success<*> ->
                    if (result.data is Cat) {
                        _catsView?.populate(result.data)
                    } else {
                        Log.w(ViewModel::class.java.toString(), "Data is not Cat")
                    }
            }
        }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job?.cancel()
    }
}
