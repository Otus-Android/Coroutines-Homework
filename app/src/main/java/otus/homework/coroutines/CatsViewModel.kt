package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {


    private var _catsView: ICatsView? = null
    private var getCatFact: Job? = null
    fun onInitComplete(context: Context) {
        getCatFact = viewModelScope.launch(CatsScope().coroutineContext) {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { catsService.getImage() }
                val catsData = CatsData(fact.await().text, image.await().file)
                _catsView?.populate(catsData)
            } catch (e: Exception) {
                when (e) {
                    is java.net.SocketTimeoutException -> {
                        Toast.makeText(
                            context,
                            "Не удалось получить ответ от сервера",
                            Toast.LENGTH_SHORT
                        )
                    }
                    else -> {
                        CrashMonitor.trackWarning(e.message)
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        getCatFact?.cancel("App closed")
        _catsView = null
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val catsService = DiContainer()
                CatsViewModel(
                    catsService.service
                )
            }
        }
    }
}