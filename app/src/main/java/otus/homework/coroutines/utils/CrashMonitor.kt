package otus.homework.coroutines.utils


import android.util.Log
import com.squareup.picasso.BuildConfig

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(exception: Throwable) {
        if(BuildConfig.DEBUG) {
            Log.i("DEBUG", exception.message.toString())
        } else {
            writeException(exception)
        }
    }
    fun writeException(exception: Throwable) {

    }
}