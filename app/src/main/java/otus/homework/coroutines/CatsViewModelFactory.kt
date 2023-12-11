package otus.homework.coroutines

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

val catsViewModelFactory = fun(
    catsFactsService: CatsFactsService,
    catsImagesService: CatsImagesService
): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            CatsViewModel(
                catsFactsService,
                catsImagesService
            )
        }
    }