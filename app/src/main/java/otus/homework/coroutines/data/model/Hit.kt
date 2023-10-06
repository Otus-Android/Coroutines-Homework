package otus.homework.coroutines.data.model

import com.google.gson.annotations.SerializedName


data class Hit(
    @SerializedName("id")
    val id: Long,
    @SerializedName("webformatURL")
    val imageURL: String
)

