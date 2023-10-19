package otus.homework.coroutines.ui

import otus.homework.coroutines.ui.model.CatContent

sealed interface ScreenState {
    class ShowContent(val content: CatContent): ScreenState

    class Error(val message: String): ScreenState
}