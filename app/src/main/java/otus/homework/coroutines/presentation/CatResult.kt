package otus.homework.coroutines.presentation

import otus.homework.coroutines.models.presentation.CatInfoModel

sealed class CatResult {
    data class Success(val catInfo: CatInfoModel) : CatResult()
    sealed class Error : CatResult(){
        data class ByString(val message:String):Error()
        data class ByRes(val stringRes:Int):Error()
    }
}