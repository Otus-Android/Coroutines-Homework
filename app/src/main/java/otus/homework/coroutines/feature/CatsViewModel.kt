package otus.homework.coroutines.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.model.CatImage
import otus.homework.coroutines.model.CatsData
import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.model.Result
import otus.homework.coroutines.model.Result.Error
import otus.homework.coroutines.model.Result.Success
import otus.homework.coroutines.retrofit.CatsImageService
import otus.homework.coroutines.retrofit.CatsService

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService,
    private val dispatcherIO: CoroutineDispatcher
): ViewModel() {

    private val catsViewModelScope: CoroutineScope = viewModelScope +
        CoroutineExceptionHandler { _, throwable -> CrashMonitor.trackWarning(throwable) }
    private val currentResult: MutableLiveData<Result> = MutableLiveData(Success(CatsData("", "")))
    fun getFactResult(): LiveData<Result> = currentResult

    fun onInitComplete() {
        catsViewModelScope.launch {
            try {
                val fact: Fact
                withContext(dispatcherIO){
                    fact = catsService.getCatFact()
                }
                currentResult.value = Success(CatsData(fact.text, ""))
            }
            catch (e: Exception){
                currentResult.value = Error(e)
            }
        }
    }

    fun onRefreshComplete() {
        catsViewModelScope.launch {
            try {
                val fact: Fact
                val catImage: CatImage
                withContext(dispatcherIO){
                    catImage = catsImageService.getCatImage()
//                    fact = catsService.getCatFact()
                }
                currentResult.value = Success(CatsData(/*fact.text*/"", catImage.file))
            }
            catch (e: Exception){
                currentResult.value = Error(e)
            }
        }
    }

}