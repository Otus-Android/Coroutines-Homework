package otus.homework.coroutines.dto

import com.google.gson.annotations.SerializedName

data class ImageFile(
    @field:SerializedName("file")
    val file: String
)
