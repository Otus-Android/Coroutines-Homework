package otus.homework.coroutines.domain

interface INetworkExceptionHandler {
	fun handleException(throwable: Throwable)
}