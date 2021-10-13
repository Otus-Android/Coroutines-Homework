package otus.homework.coroutines.services
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.squareup.picasso.OkHttp3Downloader

import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient


class ImageFetch {

    private val client = OkHttpClient.Builder().build()
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://aws.random.cat/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        val imageService by lazy { retrofit.create(ImageService::class.java) }


}