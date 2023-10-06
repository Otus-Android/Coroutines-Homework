package otus.homework.coroutines.domain.repository

import otus.homework.coroutines.data.Result
import otus.homework.coroutines.domain.model.ImageUrlModel

interface ImageUrlRepository {

    suspend fun getImageUrl(): Result<ImageUrlModel>
}