package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */

    private const val TAG = "CrashMonitor"

    fun trackWarning(msg: String) {
    }

    fun logThrowable(throwable: Throwable) {
        Log.d(TAG, "Throwable: $throwable")
    }
}