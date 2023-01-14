package otus.homework.coroutines

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private val _result = MutableStateFlow<Result>(Result.NoResult)
    val result = _result.asStateFlow()

    fun getCatsInformation() {
        try {
            viewModelScope.launch(
                CoroutineExceptionHandler { _, _ ->
                    CrashMonitor.trackWarning()
                }
            ) {
                val resultFact = withContext(Dispatchers.Default) {
                    catsService.getCatFact().body()
                }
                val resultImage = withContext(Dispatchers.Default) {
                    catsService.getImage("https://aws.random.cat/meow").body()
                }
                if (resultFact == null || resultImage == null) return@launch

                val bundle = Bundle()
                bundle.putString("fact", resultFact.fact)
                bundle.putString("image", resultImage.file)

                _result.value = Result.Success(bundle)
            }
        } catch (e: CancellationException) {
            _result.value = Result.Error(e.message.toString())
        }
    }
}
