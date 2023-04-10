package otus.homework.coroutines

import retrofit2.Response

abstract class BaseRepository {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        return try {
            val response = call()
            val body = response.body()
            if (body != null)
                Result.Success(body)
            else
                error(Exception("Body is null"))
        } catch (ex: Exception) {
            error(ex)
        }
    }

    private fun <T> error(message: Exception): Result<T> {
        return Result.Error(message)
    }

}