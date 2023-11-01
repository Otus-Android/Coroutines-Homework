package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatModel(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("url")
    val imageUrl: String,
    @field:SerializedName("width")
    val imageWidth: Int,
    @field:SerializedName("height")
    val imageHeight: Int,
)