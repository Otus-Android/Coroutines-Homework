package otus.homework.coroutines.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.service.CatsService
import otus.homework.coroutines.service.ImageService

@Suppress("UNCHECKED_CAST")
class CatsViewModelFactory(
  private val catsService: CatsService,
  private val imageService: ImageService,
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T =
    CatsViewModel(catsService, imageService) as T
}