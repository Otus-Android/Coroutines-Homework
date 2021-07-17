package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("UNCHECKED_CAST")
class DiContainer {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: CatsService by lazy { retrofit.create(CatsService::class.java) }

    val viewModelProvider by lazy {
        object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>) =
                with(modelClass) {
                    when {
                        isAssignableFrom(CatsViewModel::class.java) ->
                            CatsViewModel(service, Dispatchers.IO)
                        else ->
                            throw IllegalAccessException("Unknown ViewModel class: ${modelClass.name}")
                    }
                } as T
        }
    }

}