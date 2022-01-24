package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import otus.homework.coroutines.other.PresenterScope
import otus.homework.coroutines.other.Resource.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    companion object {
        private const val TAG = "CatsPresenter"
    }

    private var _catsView: ICatsView? = null

    val scope = PresenterScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        scope.launch(Dispatchers.IO) {
            getFacts().collect { result ->
                ensureActive()
                when (result) {
                    is Loading -> {

                    }
                    is Success -> {
                        withContext(Dispatchers.Main) {
                            result.data?.let {
                                _catsView?.populateFact(
                                    it
                                )
                            }
                        }
                    }
                    is Error -> {
                        withContext(Dispatchers.Main) {
                            result.message?.let { Log.e(TAG, it) }
                            _catsView?.showToast("Не удалось получить ответ от сервера")
                        }
                    }
                }
            }
        }
        scope.launch(Dispatchers.IO) {
            getImageUrl().collect { result ->
                ensureActive()
                when (result) {
                    is Loading -> {

                    }
                    is Success -> {
                        withContext(Dispatchers.Main) {
                            result.data?.let {
                                _catsView?.populateImage(
                                    it.file
                                )
                            }
                        }
                    }
                    is Error -> {
                        withContext(Dispatchers.Main) {
                            result.message?.let { Log.e(TAG, it) }
                            _catsView?.showToast("Не удалось получить ответ от сервера")
                        }
                    }
                }
            }
        }

    }

    fun stopFactJob() {
        if (scope.isActive)
            scope.cancel()
    }


    private suspend fun getFacts() = flow() {
        try {
            emit(Loading())
            val fact = catsService.getCatFact()
            emit(Success(fact))
        } catch (e: SocketTimeoutException) {
            emit(Error(e.message))
        }
    }

    private suspend fun getImageUrl() = flow() {
        try {
            emit(Loading())
            val imageUrl = catsService.getCatImage()
            emit(Success(imageUrl))
        } catch (e: SocketTimeoutException) {
            emit(Error(e.message))
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}