package otus.homework.coroutines.presentation

import otus.homework.coroutines.Fact
import otus.homework.coroutines.Image

data class CatsState(
    val image: Image?,
    val fact: Fact?
) {
    companion object {
        fun default() = CatsState(
            image = null,
            fact = null
        )
    }
}