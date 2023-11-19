package otus.homework.coroutines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.CatsFactService
import otus.homework.coroutines.CatsPicService

class CatsModelFactory(private val serviceFact: CatsFactService,private val servicePic: CatsPicService) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            CatsViewModel(serviceFact, servicePic) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}