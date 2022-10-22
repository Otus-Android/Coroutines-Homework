package otus.homework.coroutines

import android.util.Log

const val TAG = "CrashMonitor"

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(m: String) {
        Log.d(TAG, m)
    }
}