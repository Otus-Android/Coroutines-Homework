package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class MainVMActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val viewModel: CatsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = CatsViewModel(
                catsService = diContainer.service,
                picsService = diContainer.picsService
            ) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        val button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            viewModel.loadData()
        }

        lifecycleScope.launch {
            viewModel.dataState.collectLatest { result ->
                // индикация загрузки данных
                button.isEnabled = result !is Result.Loading
                // ошибки и успех
                when (result) {
                    // сообщить об ошибке
                    is Result.Error -> {
                        if (result.exception is SocketTimeoutException) {
                            Toast.makeText(
                                applicationContext,
                                "Не удалось получить ответ от сервера",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                result.exception.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            CrashMonitor.trackWarning(result.exception)
                        }
                    }
                    // отрисовать данные
                    is Result.Success -> {
                        view.populate(article = result.data)
                    }
                    is Result.Loading -> Unit
                }
            }
        }
    }

}