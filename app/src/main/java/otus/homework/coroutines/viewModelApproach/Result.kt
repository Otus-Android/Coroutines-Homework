package otus.homework.coroutines.viewModelApproach

import otus.homework.coroutines.CatsViewState
import otus.homework.coroutines.DisplayError

sealed class Result {

    class Success(val result: CatsViewState): Result()
    class Error(val error: DisplayError): Result()

}