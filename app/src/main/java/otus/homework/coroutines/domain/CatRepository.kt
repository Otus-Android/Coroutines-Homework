package otus.homework.coroutines.domain

import otus.homework.coroutines.domain.models.Cat

/**
 * Репозиторий информации о кошке
 */
interface CatRepository {

    /** Получить информацию о случайной кошке [Cat] */
    suspend fun getCatInfo(): Cat
}