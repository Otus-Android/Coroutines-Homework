package otus.homework.coroutines.ui

import otus.homework.coroutines.model.CatInfo

sealed class CatInfoState {
    data class Success(val serverResponseData: CatInfo) : CatInfoState()
    data class Error(val error: Throwable) : CatInfoState()
}
