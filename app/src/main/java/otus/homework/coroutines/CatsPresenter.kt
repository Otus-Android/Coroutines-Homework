package otus.homework.coroutines

import kotlinx.coroutines.*


class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService
) {

    private var _catsView: ICatsView? = null
    private val scope by lazy { PresenterScope() }
    private var job: Job? = null

    fun onStop() {
        if (job?.isActive == true) {
            job?.cancel()
        }
    }


    fun onInitComplete() {
        job = scope.launch {
            try {
                val fact: Deferred<String> = async(Dispatchers.IO) {
                    val response = catsService.getCatFact()
                    if (response.isSuccessful && response.body() != null) {
                        response.body()!!.fact
                    } else ""

                }
                val image: Deferred<String> = async(Dispatchers.IO) {
                    val response = imageService.getImage()
                    if (response.isSuccessful && response.body() != null && response.body()!!
                            .isNotEmpty()
                    ) {
                        response.body()!![0].url
                    } else ""
                }
                val factWithImage = FactWithImage(fact.await(), image.await())
                _catsView?.populate(factWithImage)
            } catch (e: java.net.SocketTimeoutException) {
                _catsView?.showToast(R.string.socket_timeout_exception_message)
            } catch (e: java.lang.Exception) {
                _catsView?.showToast("${e.message}")
            }

        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}