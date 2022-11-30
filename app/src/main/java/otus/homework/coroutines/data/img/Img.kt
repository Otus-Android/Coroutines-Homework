package otus.homework.coroutines.data.img

import com.google.gson.annotations.SerializedName

data class Img(
    @field:SerializedName("file")val imageUrl: String
)
