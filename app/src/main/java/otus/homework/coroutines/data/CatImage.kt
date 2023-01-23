package otus.homework.coroutines.data

import com.google.gson.annotations.SerializedName

data class CatImage(
    @field:SerializedName("file")
    val file: String,
)
