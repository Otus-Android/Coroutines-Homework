package otus.homework.coroutines.data.dto

import com.google.gson.annotations.SerializedName

data class ImageDto(
    @field:SerializedName("file")
    val file: String,
)