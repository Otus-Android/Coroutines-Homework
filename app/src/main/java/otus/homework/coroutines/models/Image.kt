package otus.homework.coroutines.models

import com.google.gson.annotations.SerializedName

data class Image (
    @field:SerializedName("file")
    val file: String
)