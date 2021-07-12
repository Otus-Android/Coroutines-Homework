package otus.homework.coroutines


import com.google.gson.annotations.SerializedName

data class Meme(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("caption")
    val caption: String,
    @SerializedName("category")
    val category: String
)