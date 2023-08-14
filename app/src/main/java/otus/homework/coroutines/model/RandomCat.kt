package otus.homework.coroutines.model

import com.google.gson.annotations.SerializedName

data class RandomCat(
    @field:SerializedName("_id")
    val id: String,
    @field:SerializedName("tags")
    val tags: List<String>,
    @field:SerializedName("owner")
    val owner: String,
    @field:SerializedName("createdAt")
    val createdAt: String,
    @field:SerializedName("updatedAt")
    val updatedAt: String,
    @field:SerializedName("url")
    val url: String,
    @field:SerializedName("size")
    val size: Int,
    @field:SerializedName("mimetype")
    val mimetype: String,
    @field:SerializedName("file")
    val file: String,
    @field:SerializedName("validated")
    val validated: Boolean
)
