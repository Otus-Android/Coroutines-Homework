package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val catsService: CatsService,
    private val photoService: PhotoService,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatViewViewModel::class.java)) {
            return CatViewViewModel(catsService, photoService) as T
        }
        error("Unknown ViewModel class")
    }
}