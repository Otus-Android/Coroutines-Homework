package otus.homework.coroutines.dto

import com.google.gson.annotations.SerializedName

data class Image(
  @field:SerializedName("file")
  val catImageUri: String
)