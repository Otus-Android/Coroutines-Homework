package otus.homework.coroutines.presentation

data class CatsFactState(
    val fact: String,
    val factLength: Int,
    val imageUrl: String,
) {
    companion object {
        val INIT = CatsFactState(
            fact = "",
            factLength = 0,
            imageUrl = "",
        )
    }
}