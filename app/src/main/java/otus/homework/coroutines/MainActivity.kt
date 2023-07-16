package otus.homework.coroutines

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * Базовая `activity`
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        findViewById<Button>(R.id.view_with_presenter_button).setOnClickListener {
            startActivity(
                Intent(
                    this, otus.homework.coroutines.presentation.mvp.CatsActivity::class.java
                )
            )
        }

        findViewById<Button>(R.id.view_with_view_model_button).setOnClickListener {
            startActivity(
                Intent(
                    this, otus.homework.coroutines.presentation.mvvm.CatsActivity::class.java
                )
            )
        }

        findViewById<Button>(R.id.view_with_custom_owners_button).setOnClickListener {
            startActivity(
                Intent(
                    this, otus.homework.coroutines.presentation.mvvm.owners.CatsActivity::class.java
                )
            )
        }
    }
}