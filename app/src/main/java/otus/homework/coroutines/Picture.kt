package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class Picture(
	@field:SerializedName("file")
	val pictureUrl: String
)