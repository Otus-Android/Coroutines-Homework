package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(message: String) {
        Log.d(this.javaClass.name, message)
    }
}