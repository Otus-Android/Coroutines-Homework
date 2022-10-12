package otus.homework.coroutines

import android.content.Context
import androidx.annotation.StringRes

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()

    object Loading : Result<Nothing>()

    data class Error protected constructor(
        val throwable: Throwable?,
        @StringRes private val _messageId: Int,
        private val formatArgs: List<Any?>
    ) : Result<Nothing>() {
        constructor(throwable: Throwable) : this(throwable, 0, emptyList())
        constructor(@StringRes messageId: Int, vararg formatArgs: Any?) : this(
            null,
            messageId,
            formatArgs.toList()
        )

        fun getMessage(context: Context, emptyMessage: CharSequence = ""): CharSequence = when {
            throwable != null -> throwable.message?.takeIf { it.isNotEmpty() }
                ?: throwable.javaClass.simpleName
            _messageId == 0 -> emptyMessage
            formatArgs.isEmpty() -> context.getText(_messageId)
            else -> context.getString(_messageId, *formatArgs.toTypedArray())
        }
    }
}
