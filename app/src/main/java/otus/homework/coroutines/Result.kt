package otus.homework.coroutines

sealed class Result {
    class Success<T>(val data: T): Result()

    class Error(throwable: Throwable): Result() {
        val exceptionMessage: String? =
            if (throwable is java.net.SocketTimeoutException) {
                "Не удалось получить ответ от сервером"
            } else {
                CrashMonitor.trackWarning(throwable)
                throwable.message
            }

        object CrashMonitor {
            fun trackWarning(throwable: Throwable) {}
        }
    }
}
