package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    private const val logName = "CrashMonitor"

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning() {
        Log.d(logName, "Warning received")
    }
}