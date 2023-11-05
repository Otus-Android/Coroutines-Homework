package otus.homework.coroutines.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import otus.homework.coroutines.data.api.FactApi
import otus.homework.coroutines.data.api.ImageUrlApi
import otus.homework.coroutines.di.annotations.qualifier.ApplicationContext
import otus.homework.coroutines.di.annotations.scope.ApplicationScope
import otus.homework.coroutines.di.modules.HttpsModule

@ApplicationScope
@Component(
    modules = [
        HttpsModule::class,
    ]
)
interface ApplicationComponent {

    @ApplicationContext
    fun applicationContext(): Context

    fun imageUrlApi(): ImageUrlApi

    fun factApi(): FactApi

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance @ApplicationContext applicationContext: Context,
        ): ApplicationComponent
    }
}