package otus.homework.coroutines.network.dto

import com.google.gson.annotations.SerializedName

data class CatImage(
    @SerializedName("file")
    val imageUrl: String
)
