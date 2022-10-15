package otus.homework.coroutines.network.facts.base.image

import com.google.gson.annotations.SerializedName

data class CatImageUrlFile(
    @field:SerializedName("file")
    val file: String,
)
