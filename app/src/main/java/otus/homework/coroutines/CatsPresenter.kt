package otus.homework.coroutines

import kotlinx.coroutines.*
import otus.homework.coroutines.model.CatModel

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private lateinit var presenterScope: CoroutineScope
    fun onInitComplete() {

        presenterScope =
            CoroutineScope(SupervisorJob() + CoroutineName("CatsCoroutine") + Dispatchers.Main)

        presenterScope.launch() {
            supervisorScope {
                val cat = async { catsService.getCatFact() }
                val pictureMeow = async { catsService.getPicture(url = "https://aws.random.cat/meow") }

                try {
                    _catsView?.populate(CatModel(cat.await().fact, pictureMeow.await().file))
                } catch (e: Exception) {
                    _catsView?.showException(e)
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