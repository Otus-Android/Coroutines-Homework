package otus.homework.coroutines.di

import android.app.Activity
import android.content.Context
import otus.homework.coroutines.data.CatsService

val Context.component: ApplicationComponent
    get() = (applicationContext as ApplicationComponent)

val Activity.catsService: CatsService
    get() = component.service