package otus.homework.coroutines.models

import com.google.gson.annotations.SerializedName

data class CatImage(
    @SerializedName("url")
    val url: String
)