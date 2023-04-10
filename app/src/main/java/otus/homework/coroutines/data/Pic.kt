package otus.homework.coroutines.data
import com.google.gson.annotations.SerializedName

data class Pic(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("file")
    val `file`: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("mimetype")
    val mimetype: String,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("size")
    val size: Int,
    @SerializedName("tags")
    val tags: List<String>,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("validated")
    val validated: Boolean
)