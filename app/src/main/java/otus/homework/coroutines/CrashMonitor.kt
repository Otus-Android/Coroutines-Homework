package otus.homework.coroutines

import android.util.Log

private const val TAG = "CrashMonitor"
object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(warning: String) {
        Log.d(TAG, warning)
    }
}