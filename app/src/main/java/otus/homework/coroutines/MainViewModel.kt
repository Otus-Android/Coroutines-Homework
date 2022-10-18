package otus.homework.coroutines

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private val _result = MutableStateFlow<Result>(Result.NoResult)
    val result = _result.asStateFlow()

    fun getCatsInformation() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                CrashMonitor.trackWarning()
                _result.value = Result.Error(throwable.message.toString())
            }
        ) {
            val resultFact = catsService.getCatFact().body()
            val resultImage = catsService.getImage("https://aws.random.cat/meow").body()
            if (resultFact == null || resultImage == null) return@launch

            val bundle = Bundle()
            bundle.putString("fact", resultFact.fact)
            bundle.putString("image", resultImage.file)

            _result.value = Result.Success(bundle)
        }
    }
}
