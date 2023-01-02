package otus.homework.coroutines

import kotlinx.coroutines.*
import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.data.CrashMonitor
import otus.homework.coroutines.model.CatModel
import otus.homework.coroutines.model.CatImageUrl
import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.view.ICatsView
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main + CoroutineName("CatsCoroutine")
    )

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val catModel = coroutineScope {
                    val factAsync = async(Dispatchers.IO) { catsService.getFact() }
                    val imageUrlAsync = async(Dispatchers.IO) { catsService.getImageUrl() }

                    createCatModel(factAsync.await(), imageUrlAsync.await())
                }
                catModel?.let {
                    _catsView?.populate(catModel)
                }
            } catch (throwable: Throwable) {
                when (throwable) {
                    is SocketTimeoutException -> _catsView?.showToast(R.string.error_message_response_failed)
                    else -> {
                        CrashMonitor.trackWarning(throwable)
                        throwable.message?.let { message ->
                            _catsView?.showToast(message)
                        }
                    }
                }
            }
        }
    }

    private fun createCatModel(
        factResponse: Response<Fact>,
        imageUrlResponse: Response<CatImageUrl>
    ): CatModel? {
        return if (factResponse.isSuccessful && imageUrlResponse.isSuccessful) {
            val title = factResponse.body()?.title
            val imageUrl = imageUrlResponse.body()?.url
            CatModel(title, imageUrl)
        } else null
    }
}