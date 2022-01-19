package otus.homework.coroutines.controller

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.R
import otus.homework.coroutines.facts.Fact
import otus.homework.coroutines.facts.FactsService
import otus.homework.coroutines.pictures.Picture
import otus.homework.coroutines.pictures.PicturesService
import otus.homework.coroutines.view.CatModel
import otus.homework.coroutines.view.ICatsView
import java.net.SocketTimeoutException

class CatsPresenter(
    private val factsService: FactsService,
    private val picsService: PicturesService,
) {
    private lateinit var presenterScope: PresenterScope
    private val scopeExceptionHandler = CoroutineExceptionHandler { _, ex ->
        ex.message?.let { _catsView?.showToast(it) }
        CrashMonitor.trackWarning()
    }
    private var _catsView: ICatsView? = null

    fun attachView(catsView: ICatsView) {
        presenterScope = PresenterScope()
        _catsView = catsView
    }

    fun updateData() {
        presenterScope.launch(scopeExceptionHandler) {
            val fact = async { fetchCatFact() }
            val pic = async { fetchCatPicture() }
            val model = CatModel(fact.await()?.text, pic.await()?.file)
            _catsView?.populate(model)
        }
    }

    private suspend fun fetchCatPicture(): Picture? {
        return fetchCatching { picsService.getPicture() }
    }

    private suspend fun fetchCatFact(): Fact? {
        return fetchCatching { factsService.getFact() }
    }

    private inline fun <T> fetchCatching(fetch: () -> T): T? {
        try {
            return fetch()
        } catch (e: SocketTimeoutException) {
            _catsView?.showToast(R.string.server_not_responding)
        }
        return null
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }

}
