package otus.homework.coroutines.utils

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(exceptionMessage:String) {
        Log.e("CrashMonitor",exceptionMessage)
    }
}
