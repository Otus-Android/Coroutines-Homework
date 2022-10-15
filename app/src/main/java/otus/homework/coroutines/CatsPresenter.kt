package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import otus.homework.coroutines.network.facts.abs.AbsCatService
import otus.homework.coroutines.network.facts.old.CatsService
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: AbsCatService
) {

    private var _catsView: ICatsView? = null
    private val _presenterScope = PresenterScope()
    private lateinit var catJob: Job

    fun onInitComplete() {
        catJob = _presenterScope.launch {

            try {
                val catFact = catsService.getCatFact()

                Log.d("catFact", catFact.text ?: "error")

                _catsView?.populate(catFact)

            } catch (exp: Exception) {
                if (exp is SocketTimeoutException) {
                    _catsView?.showError(R.string.socket_timeout_exception_error_text)
                } else {
                    _catsView?.showError(exp.message)
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun stopCatJob() {
        catJob.cancel()
    }
}