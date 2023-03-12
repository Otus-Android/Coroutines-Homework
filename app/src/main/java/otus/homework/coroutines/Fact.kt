package otus.homework.coroutines

data class Fact(
    val fact: String
)

data class Image(
    val file: String
)

data class Response(
    val image: String,
    val fact: String
)