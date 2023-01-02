package otus.homework.coroutines.model

import com.google.gson.annotations.SerializedName

data class Fact(
    @field:SerializedName("fact")
    val title: String,
    @field:SerializedName("length")
    val length: Int
)