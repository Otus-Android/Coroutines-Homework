package otus.homework.coroutines.mvvm

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import otus.homework.coroutines.R
import otus.homework.coroutines.data.CatDataModel
import otus.homework.coroutines.data.Result
import otus.homework.coroutines.di.DiContainer
import java.util.logging.Logger


class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            CatsViewModelFactory(diContainer.service)
        )[CatsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null)
        setContentView(view)
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.updateData()
        }
        lifecycleScope.launch {
            viewModel.result.collect { result ->
                when (result) {
                    is Result.Success<*> -> {
                        (result.data as? CatDataModel)?.let { cat ->
                            cat.fact?.let {
                                findViewById<TextView>(R.id.fact_textView).text =
                                    it.text
                            }
                            cat.picUrl?.let {
                                Picasso.get()
                                    .load(it.file)
                                    .into(findViewById<ImageView>(R.id.pic_imageView));
                            }
                        }
                    }
                    is Result.Error -> {
                        Toast.makeText(
                                this@MainActivity,
                                result.error.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                }
            }
        }
    }

}

