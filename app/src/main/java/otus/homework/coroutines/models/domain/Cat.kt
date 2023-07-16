package otus.homework.coroutines.models.domain

/**
 * Модель информации о кошке
 *
 * @param fact описание факта
 * @param image ссылка на изобрежение
 */
data class Cat(
    val fact: String,
    val image: String?
)
