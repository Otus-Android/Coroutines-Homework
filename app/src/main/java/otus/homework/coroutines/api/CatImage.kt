package otus.homework.coroutines.api

data class CatImage(
    val url: String?,
    val webpurl: String?
) {

    fun getImageUrl() = webpurl ?: url ?: ""
}