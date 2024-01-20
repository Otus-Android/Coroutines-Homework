package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MyViewModel = MyViewModel()
    private lateinit var view: CatsView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel.factAndPicture?.observe(this, Observer {
            it?.let {
                handlerResult(it)
                view.findViewById<Button>(R.id.button).isEnabled = true
            }
        })

        view.myViewModel = viewModel
        viewModel.onInitComplete()
        view.findViewById<Button>(R.id.button).isEnabled = false
    }

    fun handlerResult(result: Result<Any>) {
        when(result){
            is Result.Error -> {Toast.makeText(this, result.exception, Toast.LENGTH_LONG).show()}
            is Result.Success<Any> -> {view.populate(result.data as FactAndPicture)}
        }
    }
}