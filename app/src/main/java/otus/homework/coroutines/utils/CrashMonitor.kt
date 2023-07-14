package otus.homework.coroutines.utils

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(error: Throwable) {
        Log.e(TAG, "trackWarning: $error")
    }

    private const val TAG = "CrashMonitor"
}