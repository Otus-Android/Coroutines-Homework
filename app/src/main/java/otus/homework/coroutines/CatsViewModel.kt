package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val pictureService: PictureService): ViewModel() {


    private var jobCat: Job? = null

    val state = MutableLiveData<Result<CatModel>>()


    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        e.message?.let { CrashMonitor.trackWarning(it) }
    }

    fun onInitComplete() {

        jobCat = viewModelScope.launch(exceptionHandler) {
            try {
                val catFact = async(Dispatchers.IO) { catsService.getCatFact() }
                val picture = async(Dispatchers.IO) { pictureService.getCatPicture() }

                state.value = Result.Success(CatModel(catFact.await(), picture.await()))

            } catch (e: SocketTimeoutException) {
                state.value = Result.Error("Не удалось получить ответ от сервера")

            }
        }
    }

   fun detachView(){
       jobCat?.cancel()
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