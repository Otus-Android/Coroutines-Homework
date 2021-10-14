package otus.homework.coroutines

sealed class Result

class Success(val value: CatsInfo) : Result()
class Error(val message: String) : Result()
