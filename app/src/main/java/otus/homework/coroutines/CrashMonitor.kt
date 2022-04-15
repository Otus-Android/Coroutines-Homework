package otus.homework.coroutines

interface CrashAnalyticManager {
    fun trackWarning(exception: Throwable)
}

object CrashMonitor : CrashAnalyticManager {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    override fun trackWarning(exception: Throwable) {
    }
}