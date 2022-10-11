package otus.homework.coroutines

import android.content.Context
import androidx.annotation.StringRes

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()

    object Loading : Result<Nothing>()

    data class Error private constructor (
        private val _throwable: Throwable?,
        @StringRes private val _messageId: Int,
        private val formatArgs: List<Any?>
    ) : Result<Nothing>() {
        constructor(throwable: Throwable) : this(throwable, 0, emptyList())
        constructor(@StringRes messageId: Int, vararg formatArgs: Any?) : this(null, messageId, formatArgs.toList())

        fun getMessage(context: Context, emptyMessage: CharSequence = ""): CharSequence = when {
            _throwable != null -> _throwable.message?.takeIf { it.isNotEmpty() } ?: emptyMessage
            _messageId == 0 -> emptyMessage
            formatArgs.isEmpty() -> context.getText(_messageId)
            else -> context.getString(_messageId, *formatArgs.toTypedArray())
        }
    }
}
