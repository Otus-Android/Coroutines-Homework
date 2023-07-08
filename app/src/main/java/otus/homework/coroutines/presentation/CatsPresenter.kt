package otus.homework.coroutines.presentation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import otus.homework.coroutines.network.DiContainer
import otus.homework.coroutines.utils.PresenterScope

class CatsPresenter(
    private val catsService: DiContainer
) {

    private var _catsView: ICatsView? = null
    private val _errorsState: Channel<Throwable> = Channel()
    private val presenterScope = PresenterScope()
    val errorsState: Flow<Throwable> get() = _errorsState.receiveAsFlow()
    fun onInitComplete() {
        presenterScope.launch {
            try {
//                _catsView?.populate(
//                    fact = (catsService.choiceUrlForRetrofit(isFact = true) as? CatsService)?.getCatFact().orEmpty(),
//                    catsImage = (catsService.choiceUrlForRetrofit(isFact = false) as? CatsImageService)?.getCatImageUrl()
//                        .orEmpty()
//                )
            } catch (t: Throwable) {
                _errorsState.send(t)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.coroutineContext.job.cancel()
        _catsView = null
    }
}