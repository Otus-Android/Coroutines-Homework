package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.error.handler.CrashMonitor
import otus.homework.coroutines.network.facts.base.AbsCatService
import otus.homework.coroutines.network.facts.base.CatData
import otus.homework.coroutines.network.facts.base.image.ImageService

class MainViewModel(
    private val catsService: AbsCatService,
    private val catsImageService: ImageService
) : ViewModel() {

    private var _catsView: ICatsView? = null

    private lateinit var catFactJob: Job
    private lateinit var catImageJob: Job

    private val catData = CatData()

    private val _viewModelScope = viewModelScope + SupervisorJob() +
            CoroutineExceptionHandler { _, t ->
                CrashMonitor.trackWarning(t)
                stopCatJob()
            }

    fun onStart() {
        _viewModelScope.launch {
            catFactJob = launch {
                val catFact = catsService.getCatFact()
                catData.fact = catFact
                if (catImageJob.isCompleted) populateView()
                CrashMonitor.notCrashMessage(R.string.fact_success)
            }

            catImageJob = launch {
                val imageUrl = catsImageService.getCatImageUrl()
                catData.imageUrl = imageUrl
                if (catFactJob.isCompleted) populateView()
                CrashMonitor.notCrashMessage(R.string.image_success)
            }
        }
    }

    private fun populateView() {
        _catsView?.populate(catData)
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun onStop() {
        stopCatJob()
    }

    private fun stopCatJob() {
        catFactJob.cancel()
        catImageJob.cancel()
    }
}