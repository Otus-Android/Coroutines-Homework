package otus.homework.coroutines.api

import com.google.gson.annotations.SerializedName

data class CatImage(
    @field:SerializedName("url")
    val url: String
)