package otus.homework.coroutines.viewModelApproach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import otus.homework.coroutines.DisplayError
import otus.homework.coroutines.R

class ViewModelActivity : AppCompatActivity() {

    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        findViewById<Button>(R.id.toViewModel).visibility = View.GONE

        viewModel.state.observe(this){
            when(it){
                is Result.Error -> {
                    val toastString = when(it.error){
                        is DisplayError.Other -> it.error.message
                        DisplayError.Timeout -> "Не удалось получить ответ от сервера"
                    }
                    Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    findViewById<TextView>(R.id.fact_textView).text = it.result.fact.fact
                        Picasso.get()
                        .load(it.result.pictureUrl.pictureUrl)
                        .into(findViewById<AppCompatImageView>(R.id.fact_picture))

                }
            }
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.update()
        }

        viewModel.update()

    }

}