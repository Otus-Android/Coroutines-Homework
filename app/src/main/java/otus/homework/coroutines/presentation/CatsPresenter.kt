package otus.homework.coroutines.presentation

import kotlinx.coroutines.*
import otus.homework.coroutines.ExceptionWrapper
import otus.homework.coroutines.ExceptionWrapperImpl
import otus.homework.coroutines.services.CatImageService
import otus.homework.coroutines.services.CatsService
import otus.homework.coroutines.view.ICatsView

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: CatImageService
) : ExceptionWrapper by ExceptionWrapperImpl() {

    private val coroutineScope = PresenterScope()
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        coroutineScope.launch {
            apiCall(
                onSuccess = {
                    val facts = async { catsService.getCatFact() }
                    val image = async { imageService.getCatImage() }
                    _catsView?.populate(facts.await(), image.await())
                },
                onError = { message ->
                    _catsView?.showError(message)
                }
            )
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        coroutineScope.cancel()
    }
}