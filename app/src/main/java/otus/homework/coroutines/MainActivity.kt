package otus.homework.coroutines

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val catsViewModel: CatsViewModel by viewModels {
        viewModelFactory {
            initializer {
                CatsViewModel(
                    factsService = diContainer.factsService,
                    photoService = diContainer.photoService,
                    viewModelScope = diContainer.presenterScope
                )
            }
        }
    }

    private fun ICatsView.requestNewCatFactFromVM() {
        lifecycleScope.launch {
            when (val fetchResult = catsViewModel.fetchCatFact()) {
                is CatsState.Success -> {
                    populate(fetchResult.fact)
                }

                is CatsState.Error -> {
                    if (fetchResult.error is SocketTimeoutException) {
                        postWarning { getString(R.string.facts_timeout_message) }
                        return@launch
                    }

                    postWarning { fetchResult.error.message.toString() }
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null, false) as CatsView
        setContentView(view)
        view.attachOnButtonClickCallback { requestNewCatFactFromVM() }
        view.requestNewCatFactFromVM()
    }

}