package otus.homework.coroutines.data

import com.google.gson.annotations.SerializedName

data class Picture(
    @field:SerializedName("file")
    val file: String
)