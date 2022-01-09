package otus.homework.coroutines.utils


import com.squareup.picasso.BuildConfig

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(exception: Throwable) {
        if(BuildConfig.DEBUG) {
            println("${exception.message}")
        } else {
            writeException(exception)
        }
    }
    fun writeException(exception: Throwable) {

    }
}