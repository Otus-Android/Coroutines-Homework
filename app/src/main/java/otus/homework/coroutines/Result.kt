package otus.homework.coroutines

sealed class Result
object Loading : Result()
data class Success(val fact: TextWithPicture) : Result()
data class Error(val message: String) : Result()
