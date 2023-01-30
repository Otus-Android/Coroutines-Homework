package otus.homework.coroutines

import kotlinx.coroutines.*

class CatsPresenter(
    private val catsService: CatsService
): Presenter {

    private var _catsView: ICatsView? = null

    private lateinit var presenterScope:CoroutineScope
    fun onInitComplete() {

        presenterScope = CoroutineScope( CoroutineName("CatsCoroutine")+ Dispatchers.Main)

        presenterScope.launch() {
            try{
                val cat = catsService.getCatFact()
                _catsView?.populate(cat)
            }catch (e: Exception){
                _catsView?.showException(e)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        presenterScope.coroutineContext.cancelChildren()
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}