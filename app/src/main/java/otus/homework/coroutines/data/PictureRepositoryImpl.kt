package otus.homework.coroutines.data

import otus.homework.coroutines.data.model.Picture
import otus.homework.coroutines.data.services.PicturesService
import otus.homework.coroutines.presentation.PictureRepository
import otus.homework.coroutines.presentation.Result
import otus.homework.coroutines.presentation.model.PictureModel
import retrofit2.Retrofit
import kotlin.random.Random

class PictureRepositoryImpl(private val retrofit: Retrofit) : PictureRepository {
    override suspend fun getImage(): Result<PictureModel> {
        val pictureService = retrofit.create(PicturesService::class.java)
        return try {
            val pic =
                pictureService.getRandomPicture(PICTURE_URL + "?q=$query" + "&" + "key=$KEY" + "&" + "image_type=$IMAGE_TYPE")
            val pictureModel = PictureModel(getRandomImage(pic))
            Result.Success(data = pictureModel)
        } catch (e: Throwable) {
            Result.Error(throwable = e)
        }
    }

    private fun getRandomImage(pic: Picture): String{
       return if (pic.totalHits< PICS_ON_PAGE){
            pic.hits[Random.nextInt(pic.totalHits)].imageURL
        }else{
            pic.hits[Random.nextInt(PICS_ON_PAGE)].imageURL
       }
    }

    companion object{
        private const val PICS_ON_PAGE = 20
        private const val KEY = "15813887-db28635529fd4ce8ef9aa7dbd"
        private const val query = "cats"
        private const val IMAGE_TYPE = "photo"
        private const val PICTURE_URL = "https://pixabay.com/api/"
    }
}