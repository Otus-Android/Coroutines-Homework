package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(private val catsService: CatsService,
                    private val imagesService: ImagesService): ViewModel() {


    private var _catsView: ICatsView? = null


    fun onInitComplete() {
        viewModelScope.launch {
            try {
                val fact =async {getFact() }
                val image = async {getImg() }
                _catsView?.populate(Fact(fact.await()), Img(image.await()))
            } catch (e: Exception) {
                when (e) {
                    is CoroutineExceptionHandler -> {
                        _catsView?.message("Не удалось получить ответ от сервером")
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                    }
                }

            }
        }

    }

    private suspend fun getFact(): String {
        var res = catsService.getCatFact()
        if (res.isSuccessful && res.body() != null) {
            return res.body()!!.text
        }

        return ""
    }
    private suspend fun getImg(): String {
        var res = imagesService.getCatImg()
        if (res.isSuccessful && res.body() != null) {
            return res.body()!!.img
        }

        return ""
    }
    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    override fun onCleared() {
        super.onCleared()
        _catsView = null
    }
}