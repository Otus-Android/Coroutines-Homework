package otus.homework.coroutines.presentation.utlis

import android.content.Context
import androidx.annotation.StringRes
import otus.homework.coroutines.di.annotations.qualifier.ActivityContext
import javax.inject.Inject

class MessageProvider @Inject constructor(
    @ActivityContext private val context: Context,
) {

    operator fun invoke(@StringRes res: Int, vararg args: Any?): String {
        return context.getString(res, *args)
    }
}