package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import otus.homework.coroutines.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val viewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        with(binding){
            setContentView(root)
            button.setOnClickListener {
                it.isEnabled = false
                viewModel.loadFacts()
            }
        }

        viewModel.facts.observe(this){res ->
            when(res){
                is Success -> {
                    binding.catView.populate(res.fact)
                }
                is Error -> {
                    res.msg?.also { binding.catView.showToastMsg(it) }
                        ?: res.msgId.also{if (it>0) binding.catView.showToastMsg(it)}
                }
            }
        }
        viewModel.loadFacts()
    }
}