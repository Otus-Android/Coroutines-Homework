package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Img(
    @field:SerializedName("img") val img: Img
)
