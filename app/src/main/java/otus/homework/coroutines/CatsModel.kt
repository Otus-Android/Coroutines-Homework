package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CatsModel(
    ) : ViewModel()  {

    private val diContainer = DiContainer()
    private val diViewContainer = DiViewContainer()

    private val catsService = diContainer.service
    private val catsViewService = diViewContainer.service

//    private var text = MutableLiveData<String>()
//    val setTextView: LiveData<String>
//        get() = text
//
//    private var file = MutableLiveData<String>()
//    val setFileView: LiveData<String>
//        get() = file

    private var state = MutableLiveData<Result>()
    val setState: LiveData<Result>
        get() = state



    //run async routing
    fun getFileViewAndText(){

        viewModelScope.launch {

            val handler = CoroutineExceptionHandler { _, exception ->
                CrashMonitor.trackWarning()
                state.value = Error(exception.toString())
            }
            var lostate = Success("", "")

            val job1 = launch(handler) {
                val response = catsService.getCatFact()
                if (response.isSuccessful && response.body() != null) {
                    lostate.text = response.body()!!.text
                }
                else state.value = Error("Сервис фактов не отвечает")
            }

            val job2 = launch(handler){
                val responseView = catsViewService.getCatView()
                if (responseView.isSuccessful && responseView.body() != null) {
                    lostate.data = responseView.body()!!.file
                }
                else state.value = Error("Сервис картинок не отвечает")
            }
            job1.join()
            job2.join()
            if(lostate.text.isNotEmpty() && lostate.data.isNotEmpty() ) state.postValue(lostate)
        }
    }
}