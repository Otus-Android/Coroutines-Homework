package otus.homework.coroutines.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.data.CatResult
import otus.homework.coroutines.data.CatsFactService
import otus.homework.coroutines.data.CatsImageService
import otus.homework.coroutines.data.dto.FactDto
import otus.homework.coroutines.data.dto.ImageDto
import otus.homework.coroutines.presentation.model.FactModel
import java.net.SocketTimeoutException

class CatViewModel(
    private val catsImageService: CatsImageService,
    private val catsFactService: CatsFactService
) : ViewModel() {
    private var _catsView: ICatsView? = null

    private val exceptionHandler = CoroutineExceptionHandler { s, e ->
        CrashMonitor.trackWarning()
    }

    companion object {
        const val EMPTY_VALUE = ""
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            supervisorScope {

                val fact = async { getFact() }
                val factResult =
                    try {
                        fact.await().text
                    } catch (e: SocketTimeoutException) {
                        Log.i("11111", "catch fact ${e.message}: ")
                        _catsView?.showToast("${e.message}")
                        CatsPresenter.EMPTY_VALUE
                    }

                val image = async { getImage() }
                val imageResult =
                    try {
                        image.await().file
                    } catch (e: Exception) {
                        _catsView?.showToast("${e.message}")
                        CatsPresenter.EMPTY_VALUE
                    }


                val model = FactModel(
                    image = imageResult,
                    fact = factResult
                )

                val result = CatResult.Success(model)
                successResult(result)
            }
        }
    }

    private fun successResult(model: CatResult<FactModel>) {
        _catsView?.populate(model)
    }

    private suspend fun getFact(): FactDto {
        throw IllegalArgumentException("Какая-то страшная ошибка")
        return catsFactService.getFact()
    }

    private suspend fun getImage(): ImageDto = catsImageService.getCatImage()

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    override fun onCleared() {
        super.onCleared()
        _catsView = null
    }
}