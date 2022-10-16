package otus.homework.coroutines

import android.content.Context
import androidx.annotation.StringRes

//must be a singleton
class ResourceProvider(private val context: Context) {
    fun getString(@StringRes id: Int) = context.getString(id)
}
