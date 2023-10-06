package otus.homework.coroutines.data

sealed interface Result<Model> {

    class Success<Model>(val model: Model): Result<Model>

    class TimeoutError<Model>: Result<Model>
}