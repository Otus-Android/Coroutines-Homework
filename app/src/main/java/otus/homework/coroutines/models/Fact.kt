package otus.homework.coroutines.models

import com.google.gson.annotations.SerializedName
data class Fact(
	@SerializedName("fact")
	val fact: String,
	@SerializedName("length")
	val length : Int
)