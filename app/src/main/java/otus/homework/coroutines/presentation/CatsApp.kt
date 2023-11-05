package otus.homework.coroutines.presentation

import android.app.Application
import otus.homework.coroutines.di.components.DaggerApplicationComponent

class CatsApp: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}