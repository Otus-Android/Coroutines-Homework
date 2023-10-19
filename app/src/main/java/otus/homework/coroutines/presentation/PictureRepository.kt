package otus.homework.coroutines.presentation

import otus.homework.coroutines.presentation.model.PictureModel

interface PictureRepository {
   suspend fun getImage(): Result<PictureModel>
}