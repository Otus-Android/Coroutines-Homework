package otus.homework.coroutines.network.responses

import com.google.gson.annotations.SerializedName

data class RandomCatImageResponse(
    @SerializedName("file")
    val imageUrl: String
)
