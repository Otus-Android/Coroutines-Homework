package otus.homework.coroutines

import kotlinx.coroutines.*
import otus.homework.coroutines.model.CatModel

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private var presenterScope: CoroutineScope = CoroutineScope(SupervisorJob() + CoroutineName("CatsCoroutine") + Dispatchers.Main)

    fun onInitComplete() {

        presenterScope.launch() {
            supervisorScope {
                val cat = async { catsService.getCatFact() }
                val pictureMeow = async { catsService.getPicture(url = "https://aws.random.cat/meow") }

                try {
                    _catsView?.populate(CatModel(cat.await().fact, pictureMeow.await().file))
                } catch (e: Exception) {
                    _catsView?.showException(e)
                    if (e is CancellationException) {
                        throw e
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.coroutineContext.cancelChildren()
    }
}