package otus.homework.coroutines

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainFactory(
    val catsService: CatsService,
    val imageCatsService: ImageCatsService,
    val application: Application,
    val catMapper: CatsMapper
): ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatViewModel(catsService, imageCatsService, application, catMapper) as T
    }
}