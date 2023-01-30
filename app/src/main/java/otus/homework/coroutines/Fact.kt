package otus.homework.coroutines
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Fact(

	@SerialName("fact")
	val fact: String,

	@SerialName("length")
	val length: Int,


)