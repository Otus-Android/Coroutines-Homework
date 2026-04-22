package otus.homework.coroutines.presentation

import otus.homework.coroutines.model.CatsData
import otus.homework.coroutines.model.Result

interface ICatsView {
    fun populate(result: Result<CatsData>)
    fun showToast(message: String)
}