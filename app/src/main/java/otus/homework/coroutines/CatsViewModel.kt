package otus.homework.coroutines

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {


    var getCatFact: Job? = null
    private set

    private val catsData: MutableLiveData<Result<CatsData>> = MutableLiveData()
    val _catsData: LiveData<Result<CatsData>> = catsData
    fun onInitComplete() {
        getCatFact = viewModelScope.launch {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { catsService.getImage() }
                catsData.value = Result.Success(CatsData(fact.await().text, image.await().file))
            } catch (e: Exception) {
                catsData.value = Result.Error(e)
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val catsService = DiContainer()
                CatsViewModel(
                    catsService.service
                )
            }
        }
    }

}