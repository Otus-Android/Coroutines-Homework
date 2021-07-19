package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainVM(
    private val catsService: CatsService,
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) : ViewModel() {

    private val _memeLiveData = MutableLiveData<Result<Meme>>()
    val memeLiveData: LiveData<Result<Meme>> get() = _memeLiveData

    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { _, err ->
            _memeLiveData.postValue(Result.Error(err))
        }
    }

    init {
        getMeme()
    }

    fun getMeme() = viewModelScope.launch(exceptionHandler + coroutineContext) {
        _memeLiveData.postValue(Result.Loading())
        val meme = catsService.getCatFact()
        if (meme.isSuccessful && meme.body() != null)
            _memeLiveData.postValue(Result.Success(data = meme.body()!!))
    }
}