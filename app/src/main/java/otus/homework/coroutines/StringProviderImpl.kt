package otus.homework.coroutines

import android.content.Context

class StringProviderImpl(
    private val context: Context
) : StringProvider {

    override fun getString(res: Int) = context.getString(res)
}