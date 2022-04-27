package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var catsModel: CatsModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsModel = ViewModelProvider(this)[CatsModel::class.java]

        catsModel.state.observe(this){
            showState(it)
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            catsModel.getFileViewAndText()
        }

        catsModel.getFileViewAndText()
    }

    private fun showState( it: Result<*> ){
        if (it is Success<*>) {
            if (it.data is Fact) {
                val fact = it.data as Fact
                findViewById<TextView>(R.id.fact_textView).text = fact.text
                if (fact.source.isNotEmpty())
                    Picasso.get().load(fact.source)
                        .into(findViewById<ImageView>(R.id.fact_imageView))

            }
        }
        else Toast.makeText( this, it.data.toString(), Toast.LENGTH_LONG).show()
    }
}