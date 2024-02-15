package otus.homework.coroutines.di

import dagger.Component
import otus.homework.coroutines.presentation.CatsViewModel

@Component(modules = [ServerModule::class])
interface Component {

    fun inject(viewModel: CatsViewModel)
}