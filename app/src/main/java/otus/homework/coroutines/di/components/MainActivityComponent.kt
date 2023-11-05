package otus.homework.coroutines.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import otus.homework.coroutines.di.annotations.qualifier.ActivityContext
import otus.homework.coroutines.di.annotations.scope.ActivityScope
import otus.homework.coroutines.di.modules.PicassoModule
import otus.homework.coroutines.di.modules.RepositoriesModule
import otus.homework.coroutines.di.modules.ViewModelModule
import otus.homework.coroutines.presentation.CatsView

@ActivityScope
@Component(
    modules = [
        RepositoriesModule::class,
        ViewModelModule::class,
        PicassoModule::class,
    ],
    dependencies = [
        ApplicationComponent::class,
    ],
)
interface MainActivityComponent {

    fun inject(view: CatsView)

    @Component.Factory
    interface Factory {
        fun create(
            applicationComponent: ApplicationComponent,
            @BindsInstance @ActivityContext context: Context,
        ): MainActivityComponent
    }
}