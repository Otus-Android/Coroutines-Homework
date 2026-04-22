package otus.homework.coroutines.model

import com.google.gson.annotations.SerializedName

/**
 * Модель данных для факта о кошках.
 */
data class Fact(
    @field:SerializedName("fact")
    val text: String,
    @field:SerializedName("length")
    val length: Int
)