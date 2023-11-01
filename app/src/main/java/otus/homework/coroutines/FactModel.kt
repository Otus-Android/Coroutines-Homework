package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class FactModel(
    @field:SerializedName("fact")
    val text: String,
    @field:SerializedName("length")
    val length: Int,
)