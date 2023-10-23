package otus.homework.coroutines

import android.util.Log
import android.widget.Toast


object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(message: String) {
        Log.d("exception", message)
    }
}