package otus.homework.coroutines.di

import otus.homework.coroutines.data.CatsRepository
import otus.homework.coroutines.data.CatsService

interface ApplicationComponent {

    val catsService: CatsService

    val catsRepository: CatsRepository
}