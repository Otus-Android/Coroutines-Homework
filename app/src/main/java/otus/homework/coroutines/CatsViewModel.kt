package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val pictureService: PictureService): ViewModel() {


    private var jobCat: Job? = null

    private val _state = MutableLiveData<Result<CatModel>>()
    val state :LiveData<Result<CatModel>> = _state

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        e.message?.let { CrashMonitor.trackWarning(it) }
    }

    fun onInitComplete() {

        jobCat = viewModelScope.launch(exceptionHandler) {
            try {
                coroutineScope {
                    val catFact = async { catsService.getCatFact() }
                    val picture = async { pictureService.getCatPicture() }

                    _state.value = Result.Success(CatModel(catFact.await(), picture.await()))

                }
            } catch (e: SocketTimeoutException) {
                _state.value = Result.Error("Не удалось получить ответ от сервера")

            }
        }
    }

}
class CatsViewModelFactory(
    private val catsService: CatsService,
    private val pictureService: PictureService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CatsViewModel(catsService, pictureService) as T
    }
}