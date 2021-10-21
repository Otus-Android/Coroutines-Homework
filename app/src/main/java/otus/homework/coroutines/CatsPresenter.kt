package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService,
    private val imgService: ImgService
) {

    private var _catsView: ICatsView? = null
    private var job: Job? = null

    fun onInitComplete() {
        job = PresenterScope().launch {
            try {
                val img = PresenterScope().async(Dispatchers.IO) { getImg() }
                val txt = PresenterScope().async(Dispatchers.IO) { getTxt() }
                _catsView?.populate(Fact(txt.await(), img.await()))
            } catch (e: SocketTimeoutException) {
                _catsView?.toast("Не удалось получить ответ от сервера")
            } catch (e: UnknownHostException) {
                _catsView?.toast("Проверьте интернет-подключение")
            } catch (e: Throwable) {
                CrashMonitor.trackWarning(e)
                e.message?.let { _catsView?.toast(it) }
            }
        }
    }

    suspend fun getImg(): String {
        val resp = imgService.getCatImg()
        if (resp.isSuccessful && resp.body() != null) {
            return resp.body()!!.file
        }
        return ""
    }

    suspend fun getTxt(): String {
        val resp = catsService.getCatFact()
        if (resp.isSuccessful && resp.body() != null) {
            return resp.body()!!.text
        }
        return ""
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job?.cancel()
        _catsView = null
    }

    class PresenterScope : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + CoroutineName("CatsCoroutine")
    }
}