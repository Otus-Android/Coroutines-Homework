package otus.homework.coroutines

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageCatsService: ImageCatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(SupervisorJob() + Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {

        presenterScope.launch {
            try {
                coroutineScope {
                    val fact = async { catsService.getCatFact() }
                    val image = async { imageCatsService.getImageCatFact().first() }
                    val cat = Cat(fact.await(), image.await())
                    _catsView?.populate(cat)
                }
            }
            catch (e: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            }
            catch (e: Exception) {
                CrashMonitor.trackWarning()
                e.message?.let { _catsView?.showToast(it) }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }
}

data class Cat(
    val fact: Fact,
    val image: Image
)

data class Image(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("url")
    val url: String
)