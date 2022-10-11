package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CatsViewModel : ViewModel() {
    private val _catDescription = MutableLiveData<CatDescription?>()
    val catDescription: LiveData<CatDescription?> get() = _catDescription

    fun updateFact
}