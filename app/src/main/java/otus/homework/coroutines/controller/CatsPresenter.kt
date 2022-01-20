package otus.homework.coroutines.controller

import otus.homework.coroutines.view.CatsView
import otus.homework.coroutines.viewmodel.CatModel
import otus.homework.coroutines.viewmodel.CatsViewModel
import otus.homework.coroutines.viewmodel.Result

class CatsPresenter(private val catsView: CatsView, private val model: CatsViewModel) {

    fun onBtnClick() {
        model.updateData()
    }

    fun onStateChanged(result: Result<CatModel>) {
        when (result) {
            is Result.Success -> catsView.populate(result.value)
            is Result.Error -> onError(result)
        }
    }

    private fun onError(result: Result.Error) {
        when {
            result.msg != null -> catsView.showToast(result.msg)
            result.resId != null -> catsView.showToast(result.resId)
        }
    }

}
