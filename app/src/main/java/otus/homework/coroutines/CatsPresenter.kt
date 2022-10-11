package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

private const val TAG = "CatsPresenter"

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete(scope: CoroutineScope) {
        scope.launch {
            try {
                val responseImage = catsService.getRandomImage()
                val response = catsService.getCatFactReserve()
                _catsView?.populate(CatDescription(response.fact, responseImage.file))
//                if (response.isSuccessful && response.body() != null) {
//                    _catsView?.populate(response.body()!!.toFact())
//                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: SocketTimeoutException) {
                Log.d(TAG, "We got a timeout exception", e)
                _catsView?.showError(R.string.error_network)
            } catch (e: Exception) {
                Log.d(TAG, "We got an unknown exception", e)
                e.message.takeUnless { it.isNullOrEmpty() }?.let {
                    _catsView?.showError(it)
                } ?: run {
                    _catsView?.showError(R.string.error_unknown)
                }
                CrashMonitor.trackWarning()
            }
//            catsService.getCatFact().enqueue(object : Callback<Fact> {
//
//                override fun onResponse(call: Call<Fact>, response: Response<Fact>) {
//                    if (response.isSuccessful && response.body() != null) {
//                        _catsView?.populate(response.body()!!)
//                    }
//                }
//
//                override fun onFailure(call: Call<Fact>, t: Throwable) {
//                    CrashMonitor.trackWarning()
//                }
//            })
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}