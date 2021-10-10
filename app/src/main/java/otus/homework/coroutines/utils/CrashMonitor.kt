package otus.homework.coroutines.utils

import android.util.Log

object CrashMonitor {

    private const val TAG = "CrashMonitor"

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(message: String) {
        Log.d(TAG, message)
    }
}