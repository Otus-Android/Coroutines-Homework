package otus.homework.coroutines.data.tools

import android.util.Log

object CrashMonitor {
    private const val TAG = "CrashMonitor"

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(throwable: Throwable) {
        Log.e(TAG, throwable.message, throwable)
    }
}