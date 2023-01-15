package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    private const val TAG = "ErrorTag"
    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(message: String) {
        Log.d(TAG, message)
    }
}