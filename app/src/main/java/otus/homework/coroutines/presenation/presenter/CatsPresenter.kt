package otus.homework.coroutines.presenation.presenter

import android.util.Log
import kotlinx.coroutines.*
import otus.homework.coroutines.data.network.CatsFactService
import otus.homework.coroutines.data.network.CatsImageService
import otus.homework.coroutines.presenation.model.CatsCard
import otus.homework.coroutines.presenation.view.ICatsView

class CatsPresenter(
	private val catsFactService: CatsFactService,
	private val catsCatsImageService: CatsImageService
) {

	private val scope = CatsPresenterScope()

	private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
		_catsView?.showError(throwable)
	}

	private var job: Job? = null

	private var _catsView: ICatsView? = null

	fun getCatsFact() {
		job = scope.launch(handler) {
			val fact = getFactAsync(this).await()
			val image = getImageAsync(this).await()
			_catsView?.populate(
				CatsCard(imageUrl = image.url, factText = fact.text)
			)
		}
	}

	private suspend fun getFactAsync(scope: CoroutineScope) =
		scope.async(start = CoroutineStart.LAZY) {
			catsFactService.getCatFact()
		}


	private suspend fun getImageAsync(scope: CoroutineScope) =
		scope.async(start = CoroutineStart.LAZY) { catsCatsImageService.getImage() }


	fun release() {
		job?.cancel()
	}

	fun attachView(catsView: ICatsView) {
		_catsView = catsView
	}

	fun detachView() {
		_catsView = null
		release()
	}
}