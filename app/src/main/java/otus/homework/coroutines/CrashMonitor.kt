package otus.homework.coroutines

import android.util.Log

object CrashMonitor {
    const val TAG = "Cats Warning"

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(message: String) {
        Log.e(TAG, message)
    }
}