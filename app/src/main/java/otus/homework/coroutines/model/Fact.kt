package otus.homework.coroutines.model

import com.google.gson.annotations.SerializedName

data class Fact(
	@SerializedName("fact")
	val text: String,

	@SerializedName("length")
	val length: Int,
)