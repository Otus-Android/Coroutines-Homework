package otus.homework.coroutines.utils.di

import otus.homework.coroutines.CatsService

interface ApplicationComponent {

    val service: CatsService
}