package otus.homework.coroutines.presentation

import otus.homework.coroutines.models.presentation.CatInfoModel
import otus.homework.coroutines.models.presentation.Text

sealed class CatResult {
    data class Success(val catInfo: CatInfoModel) : CatResult()
    data class Error(val toastText: Text) : CatResult()
}