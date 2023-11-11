package otus.homework.coroutines.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import otus.homework.coroutines.presentation.FactsRepository
import otus.homework.coroutines.presentation.PictureRepository
import otus.homework.coroutines.presentation.Result
import otus.homework.coroutines.presentation.model.FactModel
import otus.homework.coroutines.presentation.model.PictureModel
import otus.homework.coroutines.ui.model.CatContent

class CatsPresenter(
    private val catsView: ICatsView,
    private val factsRepository: FactsRepository,
    private val pictureRepository: PictureRepository
) {
    init {
        getFactsByCoroutines()
    }

    fun getFactsByCoroutines() {
        CoroutineScope(Dispatchers.IO).launch {
            val jFact = async {
                factsRepository.getFact()
            }
            val jPic = async {
                pictureRepository.getImage()
            }

            withContext(Dispatchers.Main) {
                val fact =
                    when (val factResponse = jFact.await()) {
                        is Result.Success<FactModel> -> factResponse.data
                        is Result.Error -> {
                            catsView.showToast(factResponse.throwable.message ?: "error")
                            FactModel.getDefault()
                        }
                    }

                val picture =
                    when (val picResponse = jPic.await()) {
                        is Result.Success<PictureModel> -> picResponse.data
                        is Result.Error -> {
                            catsView.showToast(picResponse.throwable.message ?: "error")
                            PictureModel.getDefault()
                        }
                    }

                val content = CatContent(
                    fact = fact.text,
                    image = picture.url
                )

                if (content.image.isNotEmpty()) {
                    catsView.populate(content)
                }
            }
        }
    }
}