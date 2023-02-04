package otus.homework.coroutines
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileMeow(

	@SerialName("file")
	val file: String,

)