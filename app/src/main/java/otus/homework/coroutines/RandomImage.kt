package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class RandomImage(
    @field:SerializedName("file")
    val file: String
)
