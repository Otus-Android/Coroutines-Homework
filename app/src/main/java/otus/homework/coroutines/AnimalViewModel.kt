package otus.homework.coroutines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AnimalViewModel: ViewModel() {

    private var _liveData = MutableLiveData<Result>(Loading)
    val liveData: LiveData<Result> = _liveData

    init {
        getData()
    }

    fun getData() {

        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                CrashMonitor.trackWarning()
                _liveData.value = Error(throwable.toString())
                Log.d("Exception", throwable.toString())
            }
        ) {

            val fact = async {
                DiContainer().service.getCatFact().fact
            }

            val pict = async {
                PicturesContainer().service.getPicture().image
            }

            _liveData.value = Success(TextWithPicture(fact.await(), pict.await()))
        }
    }

}
