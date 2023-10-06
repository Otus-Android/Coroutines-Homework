package otus.homework.coroutines.di

import android.app.Activity
import android.content.Context
import android.view.View
import otus.homework.coroutines.domain.repository.FactRepository
import otus.homework.coroutines.domain.repository.ImageUrlRepository

val Context.component: ApplicationComponent
    get() = (applicationContext as ApplicationComponent)

val Activity.factRepository: FactRepository
    get() = component.factRepository

val Activity.imageUrlRepository: ImageUrlRepository
    get() = component.imageGenerateRepository

val View.picasso
    get() = context.component.picasso
