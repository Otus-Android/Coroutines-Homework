package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Image(
	@field:SerializedName("file")
	val file: String
)