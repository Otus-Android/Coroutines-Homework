package otus.homework.coroutines

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService
) {

    private var _catsView: ICatsView? = null
    private val scope = CatsScope()
    private var job: Job? = null

    fun onInitComplete() {
        job = scope.launch {
            val fact: Deferred<String> = async(Dispatchers.IO) {
                try {
                    val response = catsService.getCatFact()
                    if (response.isSuccessful && response.body() != null) {
                        response.body()!!.fact
                    } else {
                        ""
                    }
                } catch (e: SocketTimeoutException) {
                    withContext(Dispatchers.Main) {
                        _catsView?.showAlert(R.string.exception_message)
                    }
                    ""
                } catch (e: Exception) {
                    CrashMonitor.trackWarning(e)
                    withContext(Dispatchers.Main) {
                        _catsView?.showAlert("${e.message}")
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
                } catch (e: SocketTimeoutException) {
                    withContext(Dispatchers.Main) {
                        _catsView?.showAlert(R.string.exception_message)
                    }
                    ""
                } catch (e: Exception) {
                    CrashMonitor.trackWarning(e)
                    withContext(Dispatchers.Main) {
                        _catsView?.showAlert("${e.message}")
                    }
                    ""
                }

            }
            val imageUrl = image.await()
            if (imageUrl.isNotEmpty()) {
                _catsView?.populate(ImagedFact(fact.await(), imageUrl))
            }
        }
    }

        fun attachView(catsView: ICatsView) {
            _catsView = catsView
        }

        fun detachView() {
            _catsView = null
        }

        fun onStop() {
            if (job?.isActive == true) {
                job?.cancel()
            }
        }
    }
