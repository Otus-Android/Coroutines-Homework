package otus.homework.coroutines.domain

import otus.homework.coroutines.models.Cat

interface CatRepository {

    suspend fun getCatInfo(): Cat
}