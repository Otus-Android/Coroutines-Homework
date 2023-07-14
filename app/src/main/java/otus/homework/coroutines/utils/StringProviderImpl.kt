package otus.homework.coroutines.utils

import android.content.Context

class StringProviderImpl(
    private val context: Context
) : StringProvider {

    override fun getString(res: Int) = context.getString(res)

    override fun getString(res: Int, vararg args: Any) = context.getString(res, args)
}