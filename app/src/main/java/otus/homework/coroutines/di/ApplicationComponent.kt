package otus.homework.coroutines.di

import com.squareup.picasso.Picasso
import otus.homework.coroutines.domain.repository.FactRepository
import otus.homework.coroutines.data.api.CatsApi
import otus.homework.coroutines.data.api.ImageUrlGenerateApi
import otus.homework.coroutines.domain.repository.ImageUrlRepository

interface ApplicationComponent {

    val factApi: CatsApi

    val imageUrlGenerateApi: ImageUrlGenerateApi

    val factRepository: FactRepository

    val imageGenerateRepository: ImageUrlRepository

    val picasso: Picasso

}