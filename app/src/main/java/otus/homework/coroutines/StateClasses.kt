package otus.homework.coroutines

sealed class Result

class Success<T>(val result: T) : Result()

class Error(t: Throwable) : Result()