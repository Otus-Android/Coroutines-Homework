package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(e: Throwable) {
        Log.d(this.javaClass.canonicalName, "Error caught", e)
    }
}