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
            val fact: Deferred<String> = async(Dispatchers.IO) {
                try {
                    val response = catsService.getCatFact()
                    if (response.isSuccessful && response.body() != null) {
                        response.body()!!.fact
                    } else ""
                } catch (e: java.net.SocketTimeoutException) {
                    withContext(Dispatchers.Main) {
                        _catsView?.showToast(R.string.socket_timeout_exception_message)
                    }
                    ""
                } catch (e: java.lang.Exception) {
                    withContext(Dispatchers.Main) {
                        _catsView?.showToast("${e.message}")
                    }
                    ""
                }

            }
            val image: Deferred<String> = async(Dispatchers.IO) {
                try {
                    val response = imageService.getImage()
                    if (response.isSuccessful && response.body() != null && response.body()!!
                            .isNotEmpty()
                    ) {
                        response.body()!![0].url
                    } else ""
                } catch (e: java.net.SocketTimeoutException) {
                    withContext(Dispatchers.Main) {
                        _catsView?.showToast(R.string.socket_timeout_exception_message)
                    }
                    ""
                } catch (e: java.lang.Exception) {
                    withContext(Dispatchers.Main) {
                        _catsView?.showToast("${e.message}")
                    }
                    ""
                }

            }
            val factWithImage = FactWithImage(fact.await(), image.await())
            if (factWithImage.imageUrl.isNotEmpty()) {
                _catsView?.populate(factWithImage)
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