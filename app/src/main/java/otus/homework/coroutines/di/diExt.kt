package otus.homework.coroutines.di

import android.content.Context
import android.view.View

private val Context.component: ApplicationComponent
    get() = (applicationContext as ApplicationComponent)

val View.picasso
    get() = context.component.picasso

val View.viewModelFactory
    get() = context.component.viewModelFactory
