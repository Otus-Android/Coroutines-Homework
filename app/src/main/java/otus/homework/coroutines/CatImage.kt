package otus.homework.coroutines

data class CatImage(
    val url: String?,
    val webpurl: String?
) {

    fun getImageUrl() = webpurl ?: url ?: ""
}