package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
) {
    private var job: Job? = null
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        getFactsByCoroutines()
    }

    private fun getFactsByCoroutines() {
        job = CoroutineScope(Dispatchers.IO + CoroutineName(CATS_COROUTINE_NAME)).launch {
            try {
//                val fact = catsService.getCatFactWithCoroutines()
                Log.e("TAG","ddddd")
                val picture = catsService.getRandomPicture("cats")
                Log.e("TAG","eeee")
                Log.e("TAG", picture.hits[0].largeImageURL)
                withContext(Dispatchers.Main) {
//                    _catsView?.populate(fact)
                }
            } catch (ste: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    _catsView?.showToast(TIME_OUT_EXCEPTION_MESSAGE)
                }
            } catch (e: Throwable) {
                CrashMonitor.trackWarning()
                withContext(Dispatchers.Main){
                    e.message?.let{
                        _catsView?.showToast(it)
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job?.cancel()
    }
    companion object{
        private const val TIME_OUT_EXCEPTION_MESSAGE = "Не удалось получить ответ от сервером"
        private const val CATS_COROUTINE_NAME = "CatsCoroutine"
    }
}