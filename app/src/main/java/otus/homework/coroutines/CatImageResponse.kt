package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatImageResponse(
    @field:SerializedName("file")
    val fileName: String = ""
)
