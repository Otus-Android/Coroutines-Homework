package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class FactImage(
	@field:SerializedName("url")
	val url: String,
)