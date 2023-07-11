package otus.homework.coroutines.data


import otus.homework.coroutines.CatsService
import otus.homework.coroutines.DataType
import otus.homework.coroutines.domain.CatsRepository
import otus.homework.coroutines.domain.Error
import otus.homework.coroutines.domain.Result
import otus.homework.coroutines.domain.ResultException
import otus.homework.coroutines.domain.Success
import retrofit2.HttpException
import java.io.IOException

import java.net.SocketTimeoutException


class CatsRepositoryImpl(private val service: CatsService) : CatsRepository<Result<Any>> {


    override suspend fun getFact() = downloadData(DataType.FACT)


    override suspend fun getImage() = downloadData(DataType.CAT_IMAGE)


    private suspend fun downloadData(dataType: DataType): Result<Any> {
        return try {
            val data = when (dataType) {
                DataType.FACT -> service.getCatFact()
                DataType.CAT_IMAGE -> service.getCatImage()
            }
            val body = data.body()

            if (data.isSuccessful && body != null) Success(body)
            else Error(data.code(), data.message())
        } catch (e: SocketTimeoutException) {
            ResultException(SOCKET_TIMEOUT_EXCEPTION_MESSAGE)
        } catch (e: IOException) {
            ResultException(e.message)
        } catch (e: HttpException) {
            ResultException(e.message)
        } catch (e: Throwable) {
            ResultException(e.message)
        }

    }

    companion object {
        const val SOCKET_TIMEOUT_EXCEPTION_MESSAGE = "Не удалось получить ответ от сервера"

    }
}
