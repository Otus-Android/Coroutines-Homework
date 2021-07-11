package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author Юрий Польщиков on 11.07.2021
 */
class CatsViewModelFactory(
    private val catsFactService: CatsService,
    private val catsImageService: CatsImageService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            CatsViewModel(catsFactService, catsImageService) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}