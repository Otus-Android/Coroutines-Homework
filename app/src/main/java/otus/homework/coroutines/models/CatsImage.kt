package otus.homework.coroutines.models

import com.google.gson.annotations.SerializedName

data class CatsImage(
    @field:SerializedName("file")
    val file: String
)
