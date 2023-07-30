package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatsFact(
	@SerializedName("fact")
	val fact: String,
	@SerializedName("length")
	val length: String
)