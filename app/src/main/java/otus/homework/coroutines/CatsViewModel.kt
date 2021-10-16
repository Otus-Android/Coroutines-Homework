package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import otus.homework.coroutines.domain.*
import otus.homework.coroutines.presenation.model.CatsCard

class CatsViewModel(
	private val repository: ICatRepository,
	private val exceptionHandler: INetworkExceptionHandler
) : ViewModel() {


	private val _catsCardLiveData = MutableLiveData<Result<CatsCard>>()
	val catsCardLiveData: LiveData<Result<CatsCard>>
		get() = _catsCardLiveData

	private val _isShowProgressLiveData = MutableLiveData(false)
	val isShowProgressLiveData: LiveData<Boolean>
		get() = _isShowProgressLiveData

	fun getCatsFact() {
		viewModelScope.launch {
			_isShowProgressLiveData.value = true
			try {
				_catsCardLiveData.value = Success(repository.getCatCard())
				_isShowProgressLiveData.value = true
			} catch (c: CancellationException) {
				throw c
			} catch (t: Throwable) {
				exceptionHandler.handleException(t)
				_catsCardLiveData.value = Error(t)
			} finally {
				_isShowProgressLiveData.value = false
			}
		}
	}
}
