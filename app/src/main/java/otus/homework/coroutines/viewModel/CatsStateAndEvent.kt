package otus.homework.coroutines.viewModel

import otus.homework.coroutines.MeowInfo

sealed interface CatsState {
    data class Data(val meowInfo: MeowInfo) : CatsState
    object Error : CatsState
    object Init : CatsState
}

sealed interface CatsEvent {
    data class Error(val msg: String) : CatsEvent
}