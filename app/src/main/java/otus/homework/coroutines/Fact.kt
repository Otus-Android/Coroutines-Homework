package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class TextFact(
    @field:SerializedName("fact")
    val text: String
)

data class ImageFact(
    @field:SerializedName("file")
    val file: String
)

data class Fact(

    @field:SerializedName("fact")
    val text: String,

    @field:SerializedName("file")
    val file: String

)