package otus.homework.coroutines.view_models

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.DataType
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.data.CatsRepositoryImpl
import otus.homework.coroutines.domain.GetFactUseCase
import otus.homework.coroutines.domain.GetImageUseCase
import otus.homework.coroutines.domain.Success


class PresenterViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableData = MutableLiveData<Any>()

    val data get() = mutableData

    lateinit var scope: CoroutineScope


    private val fact = CatsRepositoryImpl(DiContainer(DiContainer.FACT_URL).service)
    private val catImage = CatsRepositoryImpl(DiContainer(DiContainer.IMAGE_URL).service)
    private val getFactUseCase = GetFactUseCase(fact)
    private val getImageUseCase = GetImageUseCase(catImage)


    suspend fun getDataFromNet(flag: DataType) {
        scope = CoroutineScope(Dispatchers.IO)
        var result: Any? = null
        scope.async {
            result = when (flag) {
                DataType.FACT -> getFactUseCase.getFact()
                DataType.CAT_IMAGE -> getImageUseCase.getImage()
            }
        }.await()


        result?.let {
            when (result) {
                is Success<*> -> {
                    val data = (result as Success<*>).data
                    mutableData.value = data
                }

                else -> {
                    val exc = result?.let { CrashMonitor.trackWarning(it) }
                    Toast.makeText(
                        getApplication(), exc,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }


    }


    override fun onCleared() {
        super.onCleared()
        scope.cancel()
        Log.d("PVM", "Coroutine canceled")
    }


}
