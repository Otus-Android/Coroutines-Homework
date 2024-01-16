package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService
) {

    private var _catsView: ICatsView? = null

    private lateinit var fact : Deferred<Fact>
    private lateinit var image : Deferred<Image>
    suspend fun onInitComplete() {

        try {
            withContext(Dispatchers.IO) {
                fact = async { catsService.getCatFact() }
                image = async { imageService.getCatImage().first() }
            }
//                Log.d("servise Fact", "fact ${fact.fact}")
//                Log.d("servises Image", "image ${image.url}")

                _catsView?.populate(fact.await(),image.await())

        } catch (e: Exception) {
            CrashMonitor.trackWarning(e)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}