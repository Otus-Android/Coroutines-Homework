package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.network.services.CatsService
import otus.homework.coroutines.network.services.RandomCatImageService

class CatsViewModelFactory(
    private val catsService: CatsService,
    private val randomCatImageService: RandomCatImageService,
    private val crashAnalyticManager: CrashAnalyticManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatsViewModel(
            catsService = catsService,
            randomCatImageService = randomCatImageService,
            crashMonitor = crashAnalyticManager
        ) as T
    }
}