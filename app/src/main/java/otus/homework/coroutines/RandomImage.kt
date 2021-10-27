package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class RandomImage(
    @SerializedName("file")
    val imageUrl: String
)