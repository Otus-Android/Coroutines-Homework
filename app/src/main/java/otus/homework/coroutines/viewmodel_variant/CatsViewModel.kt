package otus.homework.coroutines.viewmodel_variant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.network.CatDataRepository
import otus.homework.coroutines.network.dto.CatData
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catDataRepository: CatDataRepository
) : ViewModel() {

    private val _result = MutableLiveData<Result<CatData>?>()
    val result: LiveData<Result<CatData>?> = _result

    fun onInitComplete() {
        viewModelScope.launch {
            supervisorScope {
                try {
                    val catData = catDataRepository.request()
                    _result.value = Success(catData)
                } catch (e: SocketTimeoutException) {
                    _result.value = Error(e.message)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    e.message?.let { CrashMonitor.trackWarning(it) }
                }
            }
        }
    }
}