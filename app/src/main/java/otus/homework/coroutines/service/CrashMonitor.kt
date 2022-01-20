package otus.homework.coroutines.service

import android.util.Log

object CrashMonitor {

    private const val logName = "CrashMonitor"

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(ex: Throwable) {
        Log.d(logName, "Error received ${ex.message}")
    }
}