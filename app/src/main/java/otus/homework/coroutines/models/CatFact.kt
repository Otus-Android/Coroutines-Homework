package otus.homework.coroutines.models

import com.google.gson.annotations.SerializedName

data class CatFact(
    @field:SerializedName("fact")
    val fact: String,
    @field:SerializedName("length")
    val length: Int
)
