package otus.homework.coroutines.di.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import otus.homework.coroutines.di.annotations.qualifier.ViewModelKey
import otus.homework.coroutines.presentation.CatsViewModel

@Module
interface ViewModelModule {

    @[Binds IntoMap ViewModelKey(CatsViewModel::class)]
    fun bindCatsViewModel(impl: CatsViewModel): ViewModel
}