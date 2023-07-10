package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class ImgUrl(
	@field:SerializedName("message")
	val message: String,
	@field:SerializedName("status")
	val status: String
)