package otus.homework.coroutines.data


import otus.homework.coroutines.CatsService
import otus.homework.coroutines.domain.CatsRepository
import otus.homework.coroutines.domain.Error
import otus.homework.coroutines.domain.Result
import otus.homework.coroutines.domain.ResultException
import otus.homework.coroutines.domain.Success
import java.io.IOException

import java.net.SocketTimeoutException


class CatsRepositoryImpl(private val service: CatsService):CatsRepository<Result<Any>> {


    override suspend fun getFact(): Result<Any> {


        return try {
            val data = service.getCatFact()
            val body = data.body()

            if (data.isSuccessful && body != null) Success(body)
            else Error(data.code(), data.message())
        } catch (e: SocketTimeoutException) {
            ResultException(SOCKET_TIMEOUT_EXCEPTION_MESSAGE)
        }
        catch (e: IOException){
            ResultException(e.message)
        }


    }

    companion object{
        const val SOCKET_TIMEOUT_EXCEPTION_MESSAGE = "Не удалось получить ответ от сервера"
    }
}