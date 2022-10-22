package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Fact(
    @field:SerializedName("fact")
    val text: String,
    @field:SerializedName("length")
    val length: Int,
    val image: String = ""
)
data class File(
    val file: String
)