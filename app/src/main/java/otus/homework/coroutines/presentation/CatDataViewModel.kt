package otus.homework.coroutines.presentation

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import otus.homework.coroutines.CatsService
import otus.homework.coroutines.ICatsView
import otus.homework.coroutines.model.CatData

class CatDataViewModel(
    private val catFactService: CatsService,
    private val catImageService: CatsService,
) : ViewModel() {

    private var _catsView: ICatsView? = null

    init {
        initResponse()
    }

    private fun initResponse() {
        viewModelScope.launch {

            val catFactResponse = catFactService.getCatFact()
            val catImageResponse = catImageService.getCatImage()
            val catData = CatData(catFactResponse, catImageResponse)

            _catsView?.populate(catData)

        }
    }
}