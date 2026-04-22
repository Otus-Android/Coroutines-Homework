package otus.homework.coroutines.model

/**
 * Объединенная модель данных (Факт + Картинка).
 * Реализовано согласно пункту задания о передаче одного объекта в populate.
 */
data class CatsData(
    val fact: Fact,
    val image: CatImage
)
