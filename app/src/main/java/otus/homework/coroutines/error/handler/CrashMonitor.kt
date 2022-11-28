package otus.homework.coroutines.error.handler

import android.util.Log

object CrashMonitor {
    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(t: Throwable) {
        Log.e("cats", t.message ?: "error")
    }
}