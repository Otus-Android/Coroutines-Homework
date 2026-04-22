package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    val context: Context,
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineName("CatsCoroutine"))
    private var job: Job? = null

    fun onInitComplete() {
        job =   presenterScope.launch {
            try {
                val getCatFactDiffered = async { catsService.getCatFact() }
                val getCatImageDiffered = async { catsService.getCatImage() }

                val getCatFactResponse = getCatFactDiffered.await()
                val getCatImageResponse = getCatImageDiffered.await().firstOrNull()

                val catModelsMapper = CatModels(
                    fact = getCatFactResponse.fact,
                    url = getCatImageResponse?.url.orEmpty(),
                    width = getCatImageResponse?.width ?: 0,//значение не использую, но пусть будет
                    height = getCatImageResponse?.height ?: 0,//значение не использую, но пусть будет
                )
                _catsView?.populate(catModelsMapper)
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
                        Toast.makeText(context, R.string.timeout_error_text, Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
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
    }

    fun onStop() {
        job?.cancel()
    }
}