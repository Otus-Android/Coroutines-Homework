package otus.homework.coroutines.di

import android.app.Activity
import android.content.Context
import otus.homework.coroutines.data.CatsRepository

val Context.component: ApplicationComponent
    get() = (applicationContext as ApplicationComponent)

val Activity.catsRepository: CatsRepository
    get() = component.catsRepository
