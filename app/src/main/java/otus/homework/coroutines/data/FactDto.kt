package otus.homework.coroutines.data

import com.google.gson.annotations.SerializedName

data class FactDto(
    @SerializedName("fact")
    val fact: String,
    @SerializedName("length")
    val length: String
)
