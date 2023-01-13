package otus.homework.coroutines

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CatsViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    private val diContainer = DiContainer()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            return CatsViewModel(
                context,
                diContainer.catsFactService,
                diContainer.catsImageService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
