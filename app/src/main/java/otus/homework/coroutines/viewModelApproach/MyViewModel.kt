package otus.homework.coroutines.viewModelApproach

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CatsViewState
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.DisplayError
import otus.homework.coroutines.retrofit.CatsService
import otus.homework.coroutines.retrofit.PicturesService
import java.net.SocketTimeoutException

class MyViewModel: ViewModel() {

    private val diContainer = DiContainer()

    private val catsService: CatsService = diContainer.service
    private val picturesService: PicturesService = diContainer.picturesService

    private val _state = MutableLiveData<Result>()
    val state: LiveData<Result> = _state

    fun update(){

        viewModelScope.launch(Dispatchers.IO + handler){

            try {
                val fact = async { catsService.getCatFact() }
                val picture = async { picturesService.getRandomPicture() }

                _state.postValue(
                    Result.Success(CatsViewState(fact.await(), picture.await()))
                )

            }catch (e: SocketTimeoutException){
                CrashMonitor.trackWarning()
                _state.postValue(Result.Error(DisplayError.Timeout))
            }

        }


    }

    private val handler = CoroutineExceptionHandler { _, exception ->

        println("CoroutineExceptionHandler got $exception")
        CrashMonitor.trackWarning()

        val error = when(exception){
            is SocketTimeoutException -> {DisplayError.Timeout}
            else -> {DisplayError.Other(exception.message ?: "Unknown Error")}
        }

        _state.postValue(Result.Error(error))

    }

}