package otus.homework.coroutines

class CatsRepository constructor(private val catsService: CatsFactService, private val catsPicService: CatsPicService) {
    suspend fun getCatFact() = catsService.getCatFact()
    suspend fun getCatPic() = catsPicService.getCatPic()
}