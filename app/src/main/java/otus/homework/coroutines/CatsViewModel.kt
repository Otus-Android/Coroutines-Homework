package otus.homework.coroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import otus.homework.coroutines.services.DiContainer
import otus.homework.coroutines.tools.CrashMonitor
import java.net.SocketTimeoutException
import java.net.URL

class CatsViewModel() : ViewModel() {

    private var dataConsumer: ((CatsData) -> Unit)? = null
        set(value) {
            field = value
            value?.let {
                it(catsData)
            }
        }

    private val catsData = CatsData()
    private val diContainer = DiContainer()

    private val catsFactService = diContainer.factsService
    private val imagessService = diContainer.imagessService

    data class CatsData(
        var resultFact: String? = "",
        var resultImage: Bitmap? = null,
        var errorMessage: String? = null
    )

    init {
        doReadThings()
    }

    fun doReadThings() {
        viewModelScope.launch {
            val unit = try {
                val async1 = async {
                    val catFact = catsFactService.getCatFact()
                    catsData.resultFact = catFact.text
                }
                val async2 = async {
                    val catImageSource = imagessService.getCatImageSource()
                    withContext(Dispatchers.IO) {
                        val inputStream = URL(catImageSource.file).openStream()
                        val image = BitmapFactory.decodeStream(inputStream)
                        catsData.resultImage = image
                    }
                }
                async1.await()
                async2.await()
                dataConsumer?.let { it(catsData) }
            } catch (e: SocketTimeoutException) {
                catsData.errorMessage = "Не удалось получить ответ от сервера"
                dataConsumer?.let { it(catsData) }
            } catch (exc: Exception) {
                CrashMonitor.trackWarning(exc.message.toString())
                catsData.errorMessage = exc.message.toString()
                dataConsumer?.let { it(catsData) }
            }
        }
    }

    fun giveMeCatsData(consumer: (CatsData) -> Unit) {
        this.dataConsumer = consumer
    }

}