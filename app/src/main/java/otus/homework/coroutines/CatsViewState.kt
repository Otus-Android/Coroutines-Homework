package otus.homework.coroutines

/**
 * Модель стейта презентационного слоя для [CatsView]
 *
 * @property imageUrl ссылка на картинку
 * @property fact факт для отображения
 */
data class CatsViewState(
    val imageUrl: String?,
    val fact: Fact
)