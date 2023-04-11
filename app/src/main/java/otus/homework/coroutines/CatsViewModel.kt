package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private val _viewState = MutableLiveData<Result>()
    val viewState: LiveData<Result> = _viewState

    fun  onInitComplete(){
       viewModelScope.launch(CoroutineExceptionHandler{ _,error ->
            CrashMonitor.trackWarning()
            _viewState.value =
                Error(error.message.toString())
        }) {
                val fact = catsService.getCatFact()
                val img = imageService.getCatImg()
                _viewState.value =
                    Success<Data>(Data(fact.text, img[0].url))
        }
    }


}

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val imageService: ImageService
):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatsViewModel(catsService, imageService) as T
    }

}