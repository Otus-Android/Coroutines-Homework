package otus.homework.coroutines.data

import otus.homework.coroutines.data.model.Picture
import otus.homework.coroutines.data.services.PicturesService
import otus.homework.coroutines.presentation.PictureRepository
import retrofit2.Retrofit
import kotlin.random.Random

class PictureRepositoryImpl(private val retrofit: Retrofit) : PictureRepository{
    override suspend fun getImage(): String {
        val pictureService = retrofit.create(PicturesService::class.java)
        val pic = pictureService.getRandomPicture(Endpoints.PICTURE_URL +"?q=cats"+"&"+ "key=15813887-db28635529fd4ce8ef9aa7dbd"+"&"+"image_type=photo")
        return getRandomImage(pic)
    }

    private fun getRandomImage(pic: Picture): String{
       return if (pic.totalHits<20){
            pic.hits[Random.nextInt(pic.totalHits)].imageURL
        }else{
            pic.hits[Random.nextInt(20)].imageURL
       }
    }
}