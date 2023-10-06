package otus.homework.coroutines.presentation

interface PictureRepository {
   suspend fun getImage(): String
}