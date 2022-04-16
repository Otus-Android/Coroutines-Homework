package otus.homework.coroutines.network.dto

import com.google.gson.annotations.SerializedName

data class CatImage(
    @field:SerializedName("file")
    val imageUrl: String
)
