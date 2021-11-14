package otus.homework.coroutines

import java.lang.Exception

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(exception: Throwable) {
        println("trackWarning $exception")
    }
}