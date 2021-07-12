package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    private val mainVM by viewModels<MainVM> { ViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        view.setOnButtonClick {
            mainVM.getMeme()
        }
        observeMemes(view)
    }

    private fun observeMemes(catsView: CatsView) {
        mainVM.memeLiveData.observe(this, { result ->
            when (result) {
                is Result.Loading -> {
                }
                is Result.Error -> {
                    when (result.err) {
                        is SocketTimeoutException -> {
                            catsView.socketExceptionMessage()
                        }
                        else -> {
                            catsView.baseExceptionMessage(result.err?.message.toString())
                            CrashMonitor.trackWarning()
                        }
                    }
                }
                is Result.Success -> {
                    result.data?.let { meme ->
                        catsView.populate(meme)
                    }
                }
            }
        })
    }
}