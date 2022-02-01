package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.data.network.CatsFactService
import otus.homework.coroutines.data.network.CatsImageService
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.domain.Error
import otus.homework.coroutines.domain.Processing
import otus.homework.coroutines.domain.Result
import otus.homework.coroutines.domain.Success
import otus.homework.coroutines.presenation.model.CatsCard

class CatsViewModel() : ViewModel() {

	private val imageService: CatsImageService = DiContainer.catsImageService()
	private val factService: CatsFactService = DiContainer.catsFactService()

	private val _catsCardLiveData = MutableLiveData<Result<CatsCard>>()
	val catsCardLiveData: LiveData<Result<CatsCard>>
		get() = _catsCardLiveData

	private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
		_catsCardLiveData.value = Error(throwable)
	}

	fun getCatsFact() {
		_catsCardLiveData.value = Processing(true)

		viewModelScope.launch(handler) {
			_catsCardLiveData.value = Processing(false)

			val image = async(start = CoroutineStart.LAZY) { imageService.getImage() }.await()
			val fact = async(start = CoroutineStart.LAZY) { factService.getCatFact() }.await()

			_catsCardLiveData.value = Success(
				CatsCard(factText = fact.text, imageUrl = image.url)
			)
		}
	}
}
