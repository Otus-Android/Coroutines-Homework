package otus.homework.coroutines.presenation.presenter

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import otus.homework.coroutines.presenation.model.CatsCard
import otus.homework.coroutines.data.network.CatsFactService
import otus.homework.coroutines.data.network.CatsImageService
import otus.homework.coroutines.presenation.view.ICatsView
import java.net.SocketTimeoutException

class CatsPresenter(
	private val catsFactService: CatsFactService,
	private val catsCatsImageService: CatsImageService
) {

	private val scope = CatsPresenterScope()
	private var job: Job? = null

	private var _catsView: ICatsView? = null

	fun getCatsFact() {
		job = scope.launch {
			try {
				val fact = catsFactService.getCatFact()
				val image = catsCatsImageService.getImage()
				val card = CatsCard(fact.text, image.url)
				_catsView?.populate(card)
			} catch (s: SocketTimeoutException) {
				_catsView?.showError(s)
			} catch (c: CancellationException) {
				throw c
			} catch (t: Throwable) {
				_catsView?.showError(t)
			}
		}
	}

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