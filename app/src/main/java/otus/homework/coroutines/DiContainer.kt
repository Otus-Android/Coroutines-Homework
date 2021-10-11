package otus.homework.coroutines

import android.content.Context
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer(
    private val context: Context
) {

    val service: CatsService by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatsService::class.java)
    }

    val imageService: ImageService by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImageService::class.java)
    }

    val picasso: Picasso by lazy { Picasso.Builder(context).build() }

    val catsViewModelProvider by lazy {
        CatsViewModel.CatsViewModelProvider(service, imageService)
    }
}