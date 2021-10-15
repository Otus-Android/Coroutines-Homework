package otus.homework.coroutines

import android.util.Log
import java.lang.Exception

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(e:Exception,TAG:String) {
        Log.e(TAG,e.toString())
    }
}