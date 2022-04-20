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

        catsModel.setState.observe(this){
            showState(it)
        }

//        catsModel.setFileView.observe(this) {
//            showFileView(it)
//        }
//
//        catsModel.setFileView.observe(this) {
//            showFileView(it)
//        }
//        catsModel.setTextView.observe(this) {
//            showTextView(it)
//        }

        findViewById<Button>(R.id.button).setOnClickListener {
            catsModel.getFileViewAndText()
        }

        catsModel.getFileViewAndText()
    }

    private fun showState( it: Result ){
        if (it is Success<*>){
            findViewById<TextView>(R.id.fact_textView).text = it.text
            if (it.data is String ){
                if ((it.data as String).isNotEmpty())
                    Picasso.get().load(it.data as String).into(findViewById<ImageView>(R.id.fact_imageView))
            }
        }
        else Toast.makeText( this, it.text, Toast.LENGTH_LONG).show()
    }

//    private fun showTextView(it: String?) {
//        findViewById<TextView>(R.id.fact_textView).text = it
//    }
//
//    private fun showFileView(imageUri: String?) {
//        if (imageUri != "" && imageUri?.subSequence(0,4) == "http"){
//            val imageView = findViewById<ImageView>(R.id.fact_imageView)
//            Picasso.get().load(imageUri).into(imageView)
//            //Toast.makeText( this, "Ok", Toast.LENGTH_SHORT).show()
//        }
//        else{
//            Toast.makeText( this, imageUri, Toast.LENGTH_LONG).show()
//        }
//
//
//    }
}