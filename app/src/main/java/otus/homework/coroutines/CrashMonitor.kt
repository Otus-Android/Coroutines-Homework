package otus.homework.coroutines

import android.util.Log
import java.lang.Exception

object CrashMonitor {

    private val TAG = CrashMonitor::class.java.canonicalName
    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(e: Throwable) {
        Log.e(TAG, "Exception", e)
    }
}