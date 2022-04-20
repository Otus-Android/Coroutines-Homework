package otus.homework.coroutines

sealed class Result(open val text: String)

data class Success<T>(override var text: String, var data: T): Result(text)

data class Error(override val text: String): Result(text){

}