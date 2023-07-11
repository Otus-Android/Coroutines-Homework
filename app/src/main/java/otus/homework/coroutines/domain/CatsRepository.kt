package otus.homework.coroutines.domain

interface CatsRepository<T: Any> {

    suspend fun getFact(): T

    suspend fun getImage(): T


}