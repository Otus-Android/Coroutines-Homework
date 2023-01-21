package otus.homework.coroutines

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope


class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {


    var getCatFact: Job? = null
    private set

    private val catsData: MutableLiveData<Result<CatsData>> = MutableLiveData()
    val _catsData: LiveData<Result<CatsData>> = catsData
    fun onInitComplete() {
        getCatFact = viewModelScope.launch {
            supervisorScope {
                val fact = async { catsService.getCatFact() }
                val image = async { catsService.getImage() }
                try {
                    catsData.value = Result.Success(CatsData(fact.await().text, image.await().file))
                } catch (e: Throwable) {
                    catsData.value = Result.Error(e)
                }
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