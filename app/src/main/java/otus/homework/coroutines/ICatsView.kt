package otus.homework.coroutines

interface ICatsView {
    fun populate(data: CatPresentationData)
    fun showError(message: String)
    fun showLoading()
}