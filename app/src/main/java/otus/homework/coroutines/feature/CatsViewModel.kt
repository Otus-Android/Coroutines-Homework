package otus.homework.coroutines.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.model.CatsData
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
                currentResult.value = Success(CatsData(withContext(dispatcherIO){ catsService.getCatFact().text }, ""))
            }
            catch (e: Exception){
                currentResult.value = Error(e)
            }
        }
    }

    fun onRefreshComplete() {
        catsViewModelScope.launch {
            val catImage = async { catsImageService.getCatImage() }
            val fact = async { catsService.getCatFact() }
            try {
                currentResult.value = withContext(dispatcherIO){
                    Success(CatsData(fact.await().text, catImage.await().file))
                }
            }
            catch (e: Exception){
                currentResult.value = Error(e.cause ?: e)
            }
        }
    }

}