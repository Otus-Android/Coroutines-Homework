package otus.homework.coroutines

sealed class Result {
    data class Success(var fact: Fact) : Result()
    data class Error(var errorTxt: String) : Result()
}