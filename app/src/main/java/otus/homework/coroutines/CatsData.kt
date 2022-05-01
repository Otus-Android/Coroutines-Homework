package otus.homework.coroutines

data class CatsData(
    val text: String,
    val imageUrl: String
) {
    companion object {
        fun create(fact: Fact, image: Image) =
            CatsData(
                text = fact.text,
                imageUrl = image.file
            )
    }
}
