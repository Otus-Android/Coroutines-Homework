package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class CatsPicture(
	@field:SerializedName("file")
	val file: String
)