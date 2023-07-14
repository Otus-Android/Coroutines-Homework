package otus.homework.coroutines.models

sealed class CatState {

    object Idle : CatState()

    data class Success(val cat: Cat) : CatState()

    data class Error(val message: String) : CatState()
}