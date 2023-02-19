package otus.homework.coroutines

import kotlinx.coroutines.*
import otus.homework.coroutines.model.CatModel

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
                val pictureMeow = catsService.getPicture(url = "https://aws.random.cat/meow")
                _catsView?.populate(CatModel(cat.fact,pictureMeow.file))
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