package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning() {
    }

    fun logException(exception: Exception) {
        Log.wtf("Error", "Exception logged: ${exception.javaClass.simpleName} ${exception.message}")
        exception.printStackTrace()
    }
}