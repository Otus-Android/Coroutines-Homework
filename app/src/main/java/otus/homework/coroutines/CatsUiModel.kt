package otus.homework.coroutines

import androidx.annotation.StringRes

sealed class CatsUiModel {
    class Post(val fact: Fact, val image: Image): CatsUiModel()
    class Toast(@StringRes val messageId: Int, val varargs: Any? = null): CatsUiModel()
}
