package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(tag:String?, throwable: Throwable) {
        Log.e(tag, throwable.stackTraceToString())
    }
}