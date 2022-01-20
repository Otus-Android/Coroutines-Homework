package otus.homework.coroutines.viewmodel

import androidx.lifecycle.LiveData

interface ICatsViewModel{
    val state: LiveData<Result<CatModel>>
    fun updateData()
}
