package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import java.lang.Error
import java.net.SocketTimeoutException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImagesService: CatsImagesService
) : ViewModel() {

    companion object {

        fun factory(
            catsService: CatsService,
            catsImagesService: CatsImagesService
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CatsViewModel(catsService, catsImagesService) as T
            }
        }
    }

    private var _stateLiveData = MutableLiveData<Result<FactWithImage>>()
    val stateLiveData: LiveData<Result<FactWithImage>> get() = _stateLiveData

    init {
        update()
    }

    fun update() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _stateLiveData.value = Result.Error(Error(throwable.message))
            CrashMonitor.trackWarning()
        }) {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { catsImagesService.getCatImages().first() }

                val factWithImage = FactWithImage(
                    fact.await().fact,
                    image.await().url
                )
                _stateLiveData.value = Result.Success(factWithImage)

            } catch (e: SocketTimeoutException) {
                // По хорошему нужно реализовать подобие resourceProvider с context
                // и получать доступ к ресурсу R.string.server_not_responding
                // Однако это вне рамок задачи
                _stateLiveData.value =
                    Result.Error(Error("Не удалось получить ответ от сервера"))
            }
        }
    }
}