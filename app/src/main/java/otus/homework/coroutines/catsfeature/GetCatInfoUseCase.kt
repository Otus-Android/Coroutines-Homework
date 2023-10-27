package otus.homework.coroutines.catsfeature

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async


class GetCatInfoUseCase(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService,
) {
    suspend fun bind(actions: CatsAction, scope: CoroutineScope): CatsAction {
        val catsFactRequest = scope.async(Dispatchers.IO) { catsFactService.getCatFact() }
        val catsImagesRequest = scope.async(Dispatchers.IO) { catsImageService.getCatsImages() }
        return handle(catsFactRequest.await(), catsImagesRequest.await())
    }

    private fun handle(catFact: CatFact, catImages: List<CatImage>): CatsAction {
        Log.i("GetCatInfoUseCase", "catFact=$catFact, catImages=$catImages")
        return CatsAction.Internal.Success(CatInfo(catFact.text, catImages.first().url))
    }
}