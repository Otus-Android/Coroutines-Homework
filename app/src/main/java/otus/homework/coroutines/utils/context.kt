package otus.homework.coroutines.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(@StringRes message: Int) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()