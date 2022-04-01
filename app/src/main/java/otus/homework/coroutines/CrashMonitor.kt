package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import java.lang.Exception

object CrashMonitor {
    var context:Context? = null
    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(exception: String) {
        Toast.makeText(context, exception, Toast.LENGTH_LONG).show()
    }
}