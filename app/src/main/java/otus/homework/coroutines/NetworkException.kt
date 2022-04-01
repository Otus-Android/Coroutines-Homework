package otus.homework.coroutines

class NetworkException(
    override val message: String?,
    val code: Int
) : Exception(message)