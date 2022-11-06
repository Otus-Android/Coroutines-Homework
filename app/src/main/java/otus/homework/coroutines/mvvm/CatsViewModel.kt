package otus.homework.coroutines.mvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.data.CatDataModel
import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.data.Result
import otus.homework.coroutines.utils.CrashMonitor
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {
    private var _result: MutableSharedFlow<Result> = MutableSharedFlow(replay = 1)
    val result: Flow<Result> get() = _result

    init {
        updateData()
    }

    fun updateData() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.e("throwable", throwable.toString())
            CrashMonitor.trackWarning()
        })
        {
            try {
                val fact = this.async { catsService.getCatFact() }
                val picUrl = this.async { catsService.getPicUrl() }
                _result.tryEmit(Result.Success(CatDataModel(fact.await(), picUrl.await())))
            } catch (e: SocketTimeoutException) {
                onError(e)
            }
        }
    }

    private fun onError(throwable: Throwable) {
        _result.tryEmit(Result.Error(throwable))
    }

}