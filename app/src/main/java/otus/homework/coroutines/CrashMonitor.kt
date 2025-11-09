package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning() {

    }

    fun trackWarning(throwable: Throwable) {
        Log.d("viewModelError", throwable.toString())
    }
}