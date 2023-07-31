package otus.homework.coroutines

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import otus.homework.coroutines.Result
import java.net.SocketTimeoutException

class AnimalViewModel: ViewModel() {

    private var liveData = MutableLiveData<TextWithPicture>()

    private var state: Result = Result.Loading

    val _liveData: LiveData<TextWithPicture> = liveData

    init {
        getData()
    }

    fun getData() {

        state = Result.Loading

        viewModelScope.launch {

            val scope = CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { coroutineContext, throwable ->
                CrashMonitor.trackWarning()
                state = Result.Error
                Log.d("Exception", throwable.toString())
            })

            scope.launch {

                val fact = async {
                    DiContainer().service.getCatFact().fact
                }

                val pict = async {
                    PicturesContainer().service.getPicture().image
                }

                liveData.postValue(TextWithPicture(fact.await(), pict.await()))
                state = Result.Success
            }
        }
    }

    fun getState() = state

}
