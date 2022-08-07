package otus.homework.coroutines.models

import com.google.gson.annotations.SerializedName

data class CatImage(
    @field:SerializedName("file")
    val imageUrl: String
)