package otus.homework.coroutines.utils.coroutines

import kotlinx.coroutines.Dispatchers

class DispatcherImpl : Dispatcher {

    override val main = Dispatchers.Main
}