package otus.homework.coroutines.presentation.utlis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.domain.repository.FactRepository
import otus.homework.coroutines.domain.repository.ImageUrlRepository
import otus.homework.coroutines.presentation.CatsViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val factRepository: FactRepository,
    private val imageUrlRepository: ImageUrlRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CatsViewModel::class.java -> {
                CatsViewModel(factRepository, imageUrlRepository) as T
            }
            else -> {
                error("Unknown ViewModel class - $modelClass")
            }
        }
    }
}