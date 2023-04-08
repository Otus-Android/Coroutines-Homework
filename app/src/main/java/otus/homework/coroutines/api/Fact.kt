package otus.homework.coroutines.api

import com.google.gson.annotations.SerializedName

data class Fact(
	@field:SerializedName("fact")
	val text: String,
	@field:SerializedName("length")
	val length: String,

)