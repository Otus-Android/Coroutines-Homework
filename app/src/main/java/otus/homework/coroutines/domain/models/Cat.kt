package otus.homework.coroutines.domain.models

/**
 * Модель информации о кошке
 *
 * @property fact описание факта
 * @property image ссылка на изобрежение
 */
data class Cat(
    val fact: String,
    val image: String?
)
