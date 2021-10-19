package otus.homework.coroutines.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.data.CatsFactService
import otus.homework.coroutines.data.CatsImageService

class ViewModelFactory(
    private val catsImageService: CatsImageService,
    private val catsFactService: CatsFactService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatViewModel::class.java)) {
            return CatViewModel(catsImageService, catsFactService) as T
        }
        return modelClass.getConstructor(CatsImageService::class.java, CatsFactService::class.java)
            .newInstance()
    }
}