package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatsImage(
    @field: SerializedName("id")
    val id: String,
    @field: SerializedName("url")
    val url: String,
    @field: SerializedName("width")
    val width: String,
    @field: SerializedName("height")
    val height: String
)
