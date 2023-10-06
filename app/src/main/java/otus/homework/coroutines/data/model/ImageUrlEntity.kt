package otus.homework.coroutines.data.model

import com.google.gson.annotations.SerializedName

data class ImageUrlEntity(
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String?,
    @SerializedName("width")
    val width: String?,
    @SerializedName("height")
    val height: String?,
)