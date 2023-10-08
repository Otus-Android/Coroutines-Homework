package otus.homework.coroutines.presentation

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.CatsIconService
import otus.homework.coroutines.domain.CatsService
import otus.homework.coroutines.domain.CrashMonitor
import otus.homework.coroutines.models.domain.CatFact
import otus.homework.coroutines.models.domain.CatIcon
import otus.homework.coroutines.models.presentation.CatInfoModel
import otus.homework.coroutines.models.presentation.Text
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val iconService: CatsIconService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private lateinit var mainJob: Job

    fun onClick() {
        mainJob = presenterScope.launch {
            try {
                val fact: Deferred<CatFact> = async(Dispatchers.IO) { catsService.getCatFact() }
                val iconDeferred: Deferred<CatIcon> = async(Dispatchers.IO) { iconService.getIcons().first() }

                val icon = iconDeferred.await()
                _catsView?.populate(CatInfoModel(fact.await().text, icon.url, icon.width, icon.height))
            } catch (socketException: SocketTimeoutException) {
                _catsView?.showToast(Text.TextByRes(R.string.toast_exception))
            } catch (ex: Exception) {
                val msg = ex.message.toString()
                 CrashMonitor.trackWarning(msg)
                _catsView?.showToast(Text.TextByString(msg))
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        mainJob.cancel()
    }
}