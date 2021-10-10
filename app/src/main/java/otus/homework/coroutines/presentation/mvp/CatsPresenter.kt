package otus.homework.coroutines.presentation.mvp

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import otus.homework.coroutines.domain.CatsRepository
import otus.homework.coroutines.domain.Result
import otus.homework.coroutines.utils.CoroutineDispatchers

class CatsPresenter(
    private val catsRepository: CatsRepository,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    private var _catsView: ICatsView? = null
    private lateinit var catFactJob: Job
    private val presenterScope = CoroutineScope(coroutineDispatchers.mainDispatcher + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        catFactJob = presenterScope.launch {
            val result = withContext(coroutineDispatchers.ioDispatcher) {
                catsRepository.getCatRandomFact()
            }
            when (result) {
                is Result.Success -> _catsView?.populate(result.value)
                is Result.Error -> _catsView?.displayError(result.message)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        catFactJob.cancel()
    }
}