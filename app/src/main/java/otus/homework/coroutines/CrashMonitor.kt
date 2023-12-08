package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(e: Exception) {
        Log.e(TAG, "trackWarning: ${e.message}")
    }

    private const val TAG = "CrashMonitorTag"
}
