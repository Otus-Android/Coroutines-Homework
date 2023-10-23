package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.Result
import otus.homework.coroutines.models.CatResponse

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        val factory = MainViewModel.Companion.VMFactory(diContainer.service,diContainer.catImageService)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        viewModel.responseResult.observe(this, Observer { response ->
            Log.d("response",response.toString())
            when (response){
                is Result.Success<*> -> {
                    view.populate(response.response as CatResponse)
                    Log.d("success activity",response.response.toString())
                }
                is Result.Error -> {
                    view.toast(response.error.toString())
                    Log.d("error activity",response.error.toString())
                }
            }
        })
        newResponse()
        super.onStop()
    }
    private fun newResponse(){
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.onInitComplete()
        }
    }
}