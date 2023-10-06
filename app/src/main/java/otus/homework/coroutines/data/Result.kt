package otus.homework.coroutines.data

sealed interface Result<Model> {

    data class Success<Model>(val model: Model): Result<Model>

    data class Error<Model>(val exception: Exception): Result<Model>
}