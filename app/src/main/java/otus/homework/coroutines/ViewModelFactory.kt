package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.api.CatsRemoteDataSource
import otus.homework.coroutines.api.CatsServiceFact
import otus.homework.coroutines.api.CatsServiceImage

class ViewModelFactory(
    private val catsRemoteDataSource: CatsRemoteDataSource
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        CatsViewModel(catsRemoteDataSource) as T
}