package otus.homework.coroutines.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.model.*
import otus.homework.coroutines.model.Result.*
import otus.homework.coroutines.retrofit.CatsImageService
import otus.homework.coroutines.retrofit.CatsService
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService,
    private val dispatcherIO: CoroutineDispatcher
): ViewModel() {

    private val catsViewModelScope: CoroutineScope = viewModelScope +
        CoroutineExceptionHandler { _, throwable -> CrashMonitor.trackWarning(throwable) }
    private val currentResult: MutableLiveData<Result> by lazy { MutableLiveData(Success(CatsData("", ""))) }

    fun getFactResult(): LiveData<Result> = currentResult

    fun onInitComplete() {
        catsViewModelScope.launch {
            onMakeSomeAction(
                catsService::getCatFact,
                catsImageService::getCatImage,
                false
            )
        }
    }

    fun onRefreshComplete() {
        catsViewModelScope.launch {
            onMakeSomeAction(
                catsService::getCatFact,
                catsImageService::getCatImage,
                true
            )
        }
    }

    private suspend fun onMakeSomeAction(
        getCatFact: suspend () -> Fact?,
        getCatImage: suspend () -> CatImage?,
        flag: Boolean
    ){
        try {
            val fact: Fact?
            val catImage: CatImage?
            if (flag) {
                withContext(dispatcherIO){
                    catImage = getCatImage()
                    fact = getCatFact()
                }
                currentResult.value = Success(CatsData(/*fact?.text ?:*/ "", catImage?.file ?: ""))
            } else {
                withContext(dispatcherIO){
                    fact = catsService.getCatFact()
                }
                currentResult.value = Success(CatsData(fact?.text ?: "", ""))
            }
        }
        catch (e: SocketTimeoutException){
            currentResult.value = Error(e)
        }
        catch (e: Exception){
            currentResult.value = Error(e)
        }
    }
}