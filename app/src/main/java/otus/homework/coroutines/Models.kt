package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Fact(
	@field:SerializedName("fact")
	val fact: String
)

data class Pic(
	@field:SerializedName("url")
	val url: String
)

data class MeowInfo(
	val fact: String,
	val pic: String
)