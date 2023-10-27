package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Fact(
	@SerializedName("fact")
	val text: String,
	@SerializedName("length")
	val length: Int,
)