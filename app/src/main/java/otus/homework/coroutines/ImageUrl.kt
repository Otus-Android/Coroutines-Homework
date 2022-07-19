package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class ImageUrl(
    @field:SerializedName("file")
    val fileUrl: String,
)