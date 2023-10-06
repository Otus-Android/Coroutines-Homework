package otus.homework.coroutines.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.databinding.ActivityMainBinding
import otus.homework.coroutines.presentation.utlis.viewBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}