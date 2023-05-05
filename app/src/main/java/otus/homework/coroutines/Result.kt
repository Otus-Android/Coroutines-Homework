package otus.homework.coroutines

sealed class Result

class Success(val data: CatData) : Result()
class Error(val msg: String) : Result()
