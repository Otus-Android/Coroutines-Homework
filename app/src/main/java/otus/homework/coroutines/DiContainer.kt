package otus.homework.coroutines

import android.widget.ImageView
import com.squareup.picasso.Picasso
import otus.homework.coroutines.facts.FactsService
import otus.homework.coroutines.pictures.PicturesService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    val factsService: FactsService by lazy { factsRetrofit.create(FactsService::class.java) }

    private val factsRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/facts/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val picsService: PicturesService by lazy { picsRetrofit.create(PicturesService::class.java) }

    private val picsRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://aws.random.cat/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun setImageInto(url: String?, view: ImageView?) =
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.pic_placeholder)
            .error(R.drawable.pic_error)
            .into(view)

}