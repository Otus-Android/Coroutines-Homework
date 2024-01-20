package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class UrlPicture(
	@field:SerializedName("id")
	val id: String = "",
	@field:SerializedName("url")
	val url: String = "",
	@field:SerializedName("width")
	val width: Int = 0,
	@field:SerializedName("height")
	val height: Int = 0
)