package otus.homework.coroutines.domain

import otus.homework.coroutines.models.domain.Cat

interface CatRepository {

    suspend fun getCatInfo(): Cat
}