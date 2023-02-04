package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsPicService: CatsPicService
) : ViewModel() {

    private val _catsViewState: MutableLiveData<Result> = MutableLiveData()
    val catsViewState: LiveData<Result> = _catsViewState
    private lateinit var job: Job

    fun loadData() {
        job = viewModelScope.launch {
            _catsViewState.value = try {
                val fact = catsService.getCatFact()
                val picUrl = catsPicService.getCatPictureUrl().file
                Result.Success(mapResponse(fact, picUrl))
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> Result.Error("Не удалось получить ответ от сервером")
                    else -> Result.Error(e.message ?: "")
                }
            }
        }
    }

    fun detachView() {
        job.cancel()
    }
}