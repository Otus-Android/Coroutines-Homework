package otus.homework.coroutines

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

//    <script type="text/javascript" src="http://theoldreader.com/kittens/600/400/js"></script>

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    private val service: CatsService by lazy { retrofit.create(CatsService::class.java) }

    val repository by lazy { CatsRepository(service) }
}