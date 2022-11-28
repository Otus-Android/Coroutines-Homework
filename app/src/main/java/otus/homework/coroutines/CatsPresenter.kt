package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.network.facts.base.AbsCatService
import otus.homework.coroutines.network.facts.base.CatData
import otus.homework.coroutines.network.facts.base.image.ImageService
import java.net.SocketTimeoutException
import otus.homework.coroutines.error.handler.Result

class CatsPresenter(
    private val catsService: AbsCatService,
    private val catsImageService: ImageService
) {
    private var _catsView: ICatsView? = null
    private val _presenterScope = PresenterScope()

    fun onInitComplete() {
        _presenterScope.launch {
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

    fun detachView() {
        _catsView = null
    }

    fun stopCatJob() {
        _presenterScope.cancel()
    }
}