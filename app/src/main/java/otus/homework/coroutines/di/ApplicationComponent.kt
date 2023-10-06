package otus.homework.coroutines.di

import com.squareup.picasso.Picasso
import otus.homework.coroutines.domain.repository.FactRepository
import otus.homework.coroutines.data.api.FactApi
import otus.homework.coroutines.data.api.ImageUrlApi
import otus.homework.coroutines.domain.repository.ImageUrlRepository
import otus.homework.coroutines.presentation.utlis.ViewModelFactory

interface ApplicationComponent {

    val factApi: FactApi

    val imageUrlApi: ImageUrlApi

    val factRepository: FactRepository

    val imageUrlRepository: ImageUrlRepository

    val picasso: Picasso

    val viewModelFactory: ViewModelFactory
}