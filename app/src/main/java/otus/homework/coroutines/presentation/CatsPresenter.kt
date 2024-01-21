package otus.homework.coroutines.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import otus.homework.coroutines.util.CrashMonitor
import otus.homework.coroutines.ui.ICatsView
import otus.homework.coroutines.R
import otus.homework.coroutines.api.services.facts.IFactsService
import otus.homework.coroutines.api.services.photos.IPhotoService
import otus.homework.coroutines.dtos.Fact
import otus.homework.coroutines.dtos.Photo
import otus.homework.coroutines.presentation.entitiy.CatFact
import otus.homework.coroutines.util.dangerCast
import otus.homework.coroutines.util.runCatching
import retrofit2.HttpException
import java.lang.IllegalStateException
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: IFactsService,
    private val photoService: IPhotoService,
    private val presenterScope: CoroutineScope
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() = presenterScope.launch {
        runCatching {

            val fact = async {
                runCatching {
                    catsService.getCatFact().let {
                        it.body() ?: throw IllegalStateException(
                            "Successful response was captured with empty body",
                            HttpException(it)
                        )
                    }
                }
            }

            val photo = async {
                runCatching {
                    photoService.getRandomPhoto().let {
                        it.body() ?: throw IllegalStateException(
                            "Successful response was captured with empty body",
                            HttpException(it)
                        )
                    }
                }
            }

            val awaitedData = awaitAll(fact, photo)

            val loadedFact = awaitedData.component1()
                .getOrThrow()
                .dangerCast<Fact>()

            val loadedPhoto = awaitedData.component2()
                .getOrThrow()
                .dangerCast<List<Photo>>()
                .first()

            CatFact(
                photoUri = loadedPhoto.url,
                funFact = loadedFact.fact
            )

        }.onFailure onFailure@ {

            if (it is SocketTimeoutException) {
                _catsView?.postWarning { getString(R.string.facts_timeout_message) }
                return@onFailure
            }

            _catsView?.postWarning { it.message.toString() }
            CrashMonitor.trackWarning()

        }.onSuccess { fact ->
            _catsView?.populate(fact)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.coroutineContext.cancelChildren()
        _catsView = null
    }
}