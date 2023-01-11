package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatResultData (
    @field:SerializedName("file")
    val fileUrl: String,
    @field:SerializedName("text")
    val text: String
    )