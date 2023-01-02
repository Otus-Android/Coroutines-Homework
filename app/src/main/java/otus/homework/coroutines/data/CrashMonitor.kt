package otus.homework.coroutines.data

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(throwable: Throwable) {
        Log.e("Error log", throwable.message ?: "")
    }
}