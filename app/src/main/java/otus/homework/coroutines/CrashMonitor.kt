package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

  /**
   * Pretend this is Crashlytics/AppCenter
   */
  fun trackWarning(throwable: Throwable) {
    Log.d("asdf", "trackWarning $throwable")
  }
}