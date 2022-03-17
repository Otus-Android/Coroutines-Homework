package otus.homework.coroutines.tools

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(warn: String) {
        Log.d("CrashMonitor warning", "${warn} handled !")
    }

    fun trackError(err: String) {
        Log.d("CrashMonitor exception", "${err} handled !")
    }
}