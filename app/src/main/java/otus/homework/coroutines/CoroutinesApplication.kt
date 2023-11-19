package otus.homework.coroutines

import android.app.Application
import otus.homework.coroutines.domain.DiContainer

class CoroutinesApplication : Application() {
    // Depends on the flavor,
    val diContainer: DiContainer
        get() = DiContainer()
}