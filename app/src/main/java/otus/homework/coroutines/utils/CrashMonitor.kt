package otus.homework.coroutines.utils

import android.util.Log

object CrashMonitor {

  fun trackWarning(tag: String, throwable: Throwable) {
    Log.d(tag, throwable.message ?: "", throwable)
  }

  fun trackDebug(tag: String, msg: String) {
    Log.d(tag, msg)
  }
}