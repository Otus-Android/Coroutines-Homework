package otus.homework.coroutines

import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Main
) {

    private var _catsView: ICatsView? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception.toString())
        Toast.makeText(
            (_catsView as CatsView).context,
            exception.message,
            LENGTH_LONG
        )
            .show()
    }

    private val job = Job()
    private val catsScope =
        CoroutineScope(defaultDispatcher + CoroutineName("CatsCoroutine") + handler + job)

    private suspend fun getCatFact(): Flow<TextFact> = flow { emit(catsService.getCatFact()) }
    private suspend fun getCatImage(): Flow<ImageFact> =
        flow { emit(catsImageService.getCatImage()) }

    fun onInitComplete() {

        catsScope.launch {
            val catFactFlow = getCatFact();
            val catImageFlow = getCatImage();

            try {
                catFactFlow
                    .combine(catImageFlow) { fact, image ->
                        (Fact(fact.text, image.file))
                    }
                    .flowOn(Dispatchers.IO)
                    .catch { e ->
                        CrashMonitor.trackWarning(e.toString())
                        Toast.makeText(
                            (_catsView as CatsView).context,
                            "Не удалось получить ответ от сервера $e",
                            LENGTH_LONG)
                            .show()
                    }
                    .collect {
                        _catsView?.populate(it)
                    }
            }
            catch (e: java.net.SocketTimeoutException) {
                Toast.makeText(
                    (_catsView as CatsView).context,
                    "Не удалось получить ответ от сервера",
                    LENGTH_LONG)
                    .show()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job.cancel()
    }
}