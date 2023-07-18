package otus.homework.coroutines.domain

interface Repository {

    suspend fun getFact(): String

    suspend fun getImageUrl(): String
}
