package otus.homework.coroutines.model.network.dto

import com.google.gson.annotations.SerializedName

data class FactDto(
    @field:SerializedName("fact")
    val fact: String,
    @field:SerializedName("length")
    val length: Int
)
