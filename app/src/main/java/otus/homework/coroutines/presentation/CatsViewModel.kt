package otus.homework.coroutines.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import otus.homework.coroutines.data.network.CatsImageService
import otus.homework.coroutines.data.network.CatsService
import otus.homework.coroutines.utils.CrashMonitor
import otus.homework.coroutines.utils.Result

class CatsViewModel : ViewModel() {
    private lateinit var catsImageService: CatsImageService
    private lateinit var catsService: CatsService
    private lateinit var applicationContext: Context

    private val _state = MutableStateFlow<Result>(Result.Success(CatsFactState.INIT))
    val state: StateFlow<Result> = _state

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning()
        _state.update { Result.Error(throwable.message.orEmpty()) }
    }

    fun fetchCats() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _state.update { Result.Loading }
            val fact = catsService.getCatFact()
            val images = catsImageService.getCatImage()

            _state.update {
                Result.Success(
                    data = CatsFactState(
                        fact = fact.fact.orEmpty(),
                        factLength = fact.length ?: 0,
                        imageUrl = images.getOrNull(0)?.url.orEmpty(),
                    )
                )
            }
        }
    }

    fun onCreate(
        catsImageService: CatsImageService,
        catsService: CatsService,
        applicationContext: Context,
    ) {
        this.catsService = catsService
        this.catsImageService = catsImageService
        this.applicationContext = applicationContext
    }

    fun onRefresh() {
        fetchCats()
    }
}