package otus.homework.coroutines.presentation

import otus.homework.coroutines.entity.CatData

sealed class State

class ShowPhotoState(val catData: CatData): State()
class ErrorState(val error: String) : State()