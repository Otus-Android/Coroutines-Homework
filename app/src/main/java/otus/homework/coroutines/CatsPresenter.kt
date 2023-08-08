package otus.homework.coroutines

import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val picsService: PicturesService
) {

    private var _catsView: ICatsView? = null

    private var job: Job? = null

    fun onInitComplete() {

        _catsView?.also {
            val context = (it as CatsView).context as MainActivity

            job = context.lifecycleScope.launch {
                val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

                val fact = presenterScope.async {
                    catsService.getCatFact().fact
                }

                val pict = presenterScope.async {
                    picsService.getPicture().image
                }

                try {
                    it.populate(Success(TextWithPicture(fact.await(), pict.await())))
                }
                catch (e: SocketTimeoutException) {
                    Toast.makeText(context, "Не удалось получить ответ от сервера", Toast.LENGTH_SHORT).show()
                }
                catch (e: Exception) {
                    CrashMonitor.trackWarning()
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job?.cancel()
        _catsView = null
    }

}
