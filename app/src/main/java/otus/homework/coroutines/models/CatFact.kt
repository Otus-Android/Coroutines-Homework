package otus.homework.coroutines.models

import com.google.gson.annotations.SerializedName

data class CatFact(
    @SerializedName("fact")
    val text: String,
)