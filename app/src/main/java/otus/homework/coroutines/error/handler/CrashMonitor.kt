package otus.homework.coroutines.error.handler

import android.util.Log

object CrashMonitor {

    // TODO ResultHandler должен быть singleton
    lateinit var resultHandler : ResultHandler
    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(t: Throwable) {
        Log.e("cats", t.message ?: "error")
        resultHandler.onResult(Result.Error(t))
    }

    fun notCrashMessage(id: Int) {
        resultHandler.onResult(Result.Success(id))
    }
}