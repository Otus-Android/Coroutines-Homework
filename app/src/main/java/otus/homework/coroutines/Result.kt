package otus.homework.coroutines


sealed interface Result {
    data class Success(
        val fact: Fact,
        val link: String,
    ): Result
    data class Error(
        val error: Throwable,
    ): Result
}
