package otus.homework.coroutines.view_models

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.data.CatsRepositoryImpl
import otus.homework.coroutines.domain.Fact
import otus.homework.coroutines.domain.GetFactUseCase
import otus.homework.coroutines.domain.Success


class PresenterViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableData = MutableLiveData<Fact>()

    val data get() = mutableData

    private val scope = CoroutineScope(Dispatchers.IO)


    private val service = DiContainer().service
    private val repository = CatsRepositoryImpl(service)
    private val getFactUseCase = GetFactUseCase(repository)

    suspend fun getDataFromNet(){
        var result:Any? = null
        scope.launch {
            result = getFactUseCase.getFact()
        }.join()


        when(result){
            is Success<*> -> {
                val data = (result as Success<*>).data as Fact
                mutableData.value = data
                Log.d("PVM", data.fact )
            }

            else -> {
                val exc = result?.let { CrashMonitor.trackWarning(it) }
                Toast.makeText(getApplication(),exc,
                    Toast.LENGTH_LONG).show()
            }
        }



    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
        Log.d("PVM", "Coroutine canceled" )
    }


}