package otus.homework.coroutines.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatImage(
    @SerializedName("url")
    @Expose
    val imageUrl: String?
)
