package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import otus.homework.coroutines.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity(), CoroutineScope {

    lateinit var catsViewModel: CatsViewModel

    private val diContainer = DiContainer()

    private var job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = CoroutineName("CatsPresenterCoroutine") + Dispatchers.Main + job

    var activityMain2Binding: ActivityMain2Binding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMain2Binding =
            ActivityMain2Binding.inflate(layoutInflater)
        setContentView(activityMain2Binding?.root)

        catsViewModel = CatsViewModel(diContainer.serviceCats, diContainer.servicePhoto)
        findViewById<Button>(R.id.button).setOnClickListener {
            showLoading()
            catsViewModel.onInitComplete()
        }

        launch(Dispatchers.Main) {
            catsViewModel.catsInfo.collect { result ->
                when (result) {
                    is Result.Error -> {
                        when (result.throwable) {
                            is SocketTimeoutException -> {
                                showMessage(Message(stringId = R.string.socket_network_error))
                            }
                            else -> {
                                result.throwable.message?.let {
                                    showMessage(it)
                                }
                                CrashMonitor.trackWarning()
                            }
                        }
                        showContent()
                    }
                    is Result.Success -> {
                        populate(result.data.fact, result.data.photo)
                    }
                }
            }
        }
    }

    private fun populate(fact: Fact, photo: Photo) {
        activityMain2Binding?.factTextView?.text = fact.text
        Picasso.get().load(photo.file).into(activityMain2Binding?.imageView, object : Callback {
            override fun onSuccess() {
                showContent()
            }

            override fun onError(e: Exception?) {
                showContent()
                showMessage(Message(R.string.socket_network_error))
            }

        })
    }

    private fun showContent() {
        activityMain2Binding?.progressBar?.isGone = true
        activityMain2Binding?.mainGroup?.isGone = false
    }

    private fun showLoading() {
        activityMain2Binding?.progressBar?.isGone = false
        activityMain2Binding?.mainGroup?.isGone = true
    }

    private fun showMessage(message: Message) {
        Toast.makeText(this, message.stringId, Toast.LENGTH_SHORT).show()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        showLoading()
        catsViewModel.onInitComplete()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancelChildren()
    }
}