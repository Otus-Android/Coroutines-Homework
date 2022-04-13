package otus.homework.coroutines

interface CrashAnalyticManager {
    fun trackWarning()
}

object CrashMonitor: CrashAnalyticManager {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    override fun trackWarning() {
    }
}