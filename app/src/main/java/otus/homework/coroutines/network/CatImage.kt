package otus.homework.coroutines.network

import com.google.gson.annotations.SerializedName

data class CatImage(
    @field:SerializedName("file")
    val catImage: String
)