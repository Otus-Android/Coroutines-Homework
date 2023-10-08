package otus.homework.coroutines.domain

import otus.homework.coroutines.data.CatFactsService
import otus.homework.coroutines.data.CatImagesService

class CatRepository(
    private val catFactsService: CatFactsService,
    private val catImagesService: CatImagesService
) {
}