package otus.homework.coroutines

import android.util.Log

private const val TAG = "CrashMonitor"
object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(e: Exception) {
        Log.d(TAG, " get Exception $e")
    }
}