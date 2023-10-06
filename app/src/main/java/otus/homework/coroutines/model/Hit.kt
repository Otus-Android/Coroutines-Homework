package otus.homework.coroutines.model

import com.google.gson.annotations.SerializedName


data class Hit(
    @SerializedName("id")
    val id: Long,
    @SerializedName("largeImageURL")
    val largeImageURL: String
)

