package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import otus.homework.coroutines.databinding.ActivityMainBinding
import otus.homework.coroutines.viewmodel.CatsModelFactory
import otus.homework.coroutines.viewmodel.CatsViewModel


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: CatsViewModel
    private val diContainer = DiContainer()
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel =
            ViewModelProvider(this, CatsModelFactory(diContainer.serviceFact, diContainer.servicePic)).get(CatsViewModel::class.java)

        binding.button.setOnClickListener {
            runBlocking {
                launch {
                    viewModel.onInitComplete()
                }
            }
        }

        viewModel.catFactLiveData.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.factTextView.text = result.value.fact
                    binding.catImage.setImageBitmap(result.value.picUrl)
                }

                is Result.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.onInitComplete()
        }
    }

    override fun onStop() {
        if (isFinishing) {
            viewModel.onCleared()
        }
        super.onStop()
    }

}