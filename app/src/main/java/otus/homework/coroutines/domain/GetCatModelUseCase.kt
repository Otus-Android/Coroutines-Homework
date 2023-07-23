package otus.homework.coroutines.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class GetCatModelUseCase(private val repository: Repository) {

    suspend operator fun invoke(): CatModel = withContext(Dispatchers.IO) {
        val fact = async { repository.getFact() }
        val url = async { repository.getImageUrl() }
        CatModel(fact.await(), url.await())
    }
}
