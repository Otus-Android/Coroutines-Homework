package otus.homework.coroutines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import otus.homework.coroutines.presentation.presenter.ViewWithPresenterActivity
import otus.homework.coroutines.presentation.viewmodel.ViewWithViewModelActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        findViewById<Button>(R.id.view_with_presenter_button).setOnClickListener {
            startActivity(Intent(this, ViewWithPresenterActivity::class.java))
        }

        findViewById<Button>(R.id.view_with_view_model_button).setOnClickListener {
            startActivity(Intent(this, ViewWithViewModelActivity::class.java))
        }
    }
}