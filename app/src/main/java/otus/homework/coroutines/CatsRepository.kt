package otus.homework.coroutines

class CatsRepository(private val apiInterface: CatsService) {
    suspend fun getCatImage() : Result<Image>{
        return CustomNetworkCall.safeApiCall {
            apiInterface.getCatImage()
        }
    }

    suspend fun getCatFact() : Result<Fact>{
        return CustomNetworkCall.safeApiCall {
            apiInterface.getCatFact()
        }
    }
}