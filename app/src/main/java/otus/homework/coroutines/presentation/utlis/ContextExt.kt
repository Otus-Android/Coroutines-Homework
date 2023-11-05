package otus.homework.coroutines.presentation.utlis

import android.content.Context
import otus.homework.coroutines.presentation.CatsApp

@Suppress("UNCHECKED_CAST")
fun <T> Context.activityComponent(): T {
    return (this as ComponentHolder<T>).component
}

val Context.applicationComponent
    get() = (applicationContext as CatsApp).component