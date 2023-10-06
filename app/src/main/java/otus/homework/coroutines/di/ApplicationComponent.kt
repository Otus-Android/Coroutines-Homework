package otus.homework.coroutines.di

import otus.homework.coroutines.data.CatsService

interface ApplicationComponent {

    val service: CatsService
}