package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

data class FileCats(
	@field:SerializedName("file")
	val file: String,
)
