package otus.homework.coroutines.di.modules

import dagger.Binds
import dagger.Module
import otus.homework.coroutines.data.reposiroty.FactRepositoryImpl
import otus.homework.coroutines.data.reposiroty.ImageUrlRepositoryImpl
import otus.homework.coroutines.di.annotations.scope.ActivityScope
import otus.homework.coroutines.domain.repository.FactRepository
import otus.homework.coroutines.domain.repository.ImageUrlRepository

@Module
interface RepositoriesModule {

    @[Binds ActivityScope]
    fun bindFactRepository(impl: FactRepositoryImpl): FactRepository

    @[Binds ActivityScope]
    fun bindImageUrlRepository(impl: ImageUrlRepositoryImpl): ImageUrlRepository
}