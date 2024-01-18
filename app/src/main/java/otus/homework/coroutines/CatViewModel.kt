package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatViewModel(private val catsService: CatsService):ViewModel() {
    private var _catsView: ICatsView? = null
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning("${throwable.localizedMessage}")
        }
    fun getCat(){
        viewModelScope.launch(Dispatchers.Main + CoroutineName("CatsCoroutine")+coroutineExceptionHandler){
            val fact = async {
                return@async try {
                    catsService.getCatFact()
                } catch (e: Exception) {
                    when (e) {
                        is SocketTimeoutException -> _catsView?.showError("Не удалось получить ответ от сервером")
                        else -> {
                            _catsView?.showError(e.message.toString())
                        }
                    }
                } as Fact
            }.await()
            val img = async {
                return@async try {
                    catsService.getCatFactImg().first()
                } catch (e: Exception) {
                    when (e) {
                        is SocketTimeoutException -> _catsView?.showError("Не удалось получить ответ от сервером")
                        else -> {
                            _catsView?.showError(e.message.toString())
                        }
                    }
                } as Img
            }.await()

            _catsView?.populate(fact, img)
        }
    }
}