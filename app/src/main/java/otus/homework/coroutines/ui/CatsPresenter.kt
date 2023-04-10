package otus.homework.coroutines.ui

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import otus.homework.coroutines.ICatsView
import otus.homework.coroutines.usecase.CatInfoUseCase
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catInfoUseCase: CatInfoUseCase
) {

    private var _catsView: ICatsView? = null

    private val presenterCoroutineScope = CoroutineScope(
        Dispatchers.Main + CoroutineName("CatsCoroutine")
    )

    fun onInitComplete() {
        presenterCoroutineScope.launch {
            try {
                val catInfo = catInfoUseCase.getFactAndPicture()
                _catsView?.populate(catInfo)
            } catch (socketTimeoutException: SocketTimeoutException) {
                _catsView?.showTimeoutMessage()
            } catch (exception: Exception) {
                _catsView?.showMessage(exception.message)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterCoroutineScope.coroutineContext.cancelChildren()
    }
}
