package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatDetails(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("created_at")
    val createdAt: String,
    @field:SerializedName("tags")
    val tags: List<String> = emptyList(),
    @field:SerializedName("url")
    val url: String
)