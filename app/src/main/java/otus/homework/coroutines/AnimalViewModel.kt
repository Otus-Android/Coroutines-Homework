package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AnimalViewModel: ViewModel() {

    private var _liveData = MutableLiveData<Result>(Loading)
    val liveData: LiveData<Result> = _liveData

    init {
        getData()
    }

    fun getData() {

        viewModelScope.launch {

            val scope = CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
                CrashMonitor.trackWarning()
                _liveData.postValue(Error(throwable.toString()))
                Log.d("Exception", throwable.toString())
            })

            scope.launch {

                val fact = async {
                    DiContainer().service.getCatFact().fact
                }

                val pict = async {
                    PicturesContainer().service.getPicture().image
                }

                _liveData.postValue(Success(TextWithPicture(fact.await(), pict.await())))
            }
        }
    }

}
