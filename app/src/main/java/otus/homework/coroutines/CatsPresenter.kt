package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.net.SocketTimeoutException

class CatsPresenter(
    val context: Context,
    private val catsService: CatsService,
    private val imageService: ImageService,
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(Dispatchers.Main)
        .plus(CoroutineName("CatsCoroutine"))

    @Suppress("UNCHECKED_CAST")
    fun onInitComplete() {
        val handler = CoroutineExceptionHandler { _, _ ->
            CrashMonitor.trackWarning()
        }
        presenterScope.launch(handler) {
            val fact = async{
                catchingErrors { catsService.getCatFact() }
            }
            val image = async{
                catchingErrors { imageService.getCatImage() }
            }
            when (val fact = fact.await()) {
                is Success<*> -> {
                    Cat(
                        fact = (fact.result as Fact).fact,
                        url = when (val image = image.await()) {
                            is Success<*> -> (image.result as? List<CatImage>)?.firstOrNull()?.url
                            is Error -> null
                        }
                    ).let {
                        _catsView?.populate(it)
                    }
                }
                is Error -> {}
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
        presenterScope.cancel()
    }

    inline fun <T> catchingErrors(block: ()-> T): Result =
         try {
             Success(block())
        } catch (e: Exception) {
            if (e is SocketTimeoutException)
                Toast.makeText(
                    context,
                    R.string.socket_timeout_exception,
                    Toast.LENGTH_SHORT
                ).show()
            else {
                CrashMonitor.trackWarning()
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
             Error(e.message)
        }
}