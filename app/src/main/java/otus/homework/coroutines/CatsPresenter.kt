package otus.homework.coroutines

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsPresenter(
    private val catsService: CatsService,
    private val awsService: CatsService
) {

    private var _catsView: ICatsView? = null

    suspend fun onInitComplete() {
        val fact = catsService.getCatFact()
        fact.imgUrl = awsService.getCatPicture()
        _catsView?.populate(fact)
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}