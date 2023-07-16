package otus.homework.coroutines.domain

import otus.homework.coroutines.models.domain.Cat

/**
 * Репозиторий информации о кошке
 */
interface CatRepository {

    /** Получить информацию о случайной кошке [Cat] */
    suspend fun getCatInfo(): Cat
}