package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Meow(
    @field:SerializedName("file")
    val imageUrl: String,
)