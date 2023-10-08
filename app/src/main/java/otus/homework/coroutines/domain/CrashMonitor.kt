package otus.homework.coroutines.domain

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(msg: String) {
        Log.w(TAG, msg)
    }

    private const val TAG = "CrashMonitor"
}