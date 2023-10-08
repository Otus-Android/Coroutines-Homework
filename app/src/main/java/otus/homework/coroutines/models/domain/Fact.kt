package otus.homework.coroutines.models.domain

import com.google.gson.annotations.SerializedName

data class CatFact(
    @SerializedName("fact")
    val text: String,
    @SerializedName("deleted")
    val length: Int
)

data class CatIcon(
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int
)