package otus.homework.coroutines.di

import android.content.Context
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import otus.homework.coroutines.R
import otus.homework.coroutines.data.network.CatsFactService
import otus.homework.coroutines.data.network.CatsImageService
import otus.homework.coroutines.domain.CrashMonitor
import otus.homework.coroutines.domain.INetworkExceptionHandler
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

object DiContainer {

	fun catsFactService() = retrofit("https://cat-fact.herokuapp.com/facts/")
		.create(CatsFactService::class.java)

	fun catsImageService() = retrofit("https://aws.random.cat/")
		.create(CatsImageService::class.java)

	private fun retrofit(baseUrl: String, timeout: Long = 30): Retrofit {
		val loggingInterceptor = HttpLoggingInterceptor().apply {
			level = HttpLoggingInterceptor.Level.BODY
		}

		val client = OkHttpClient.Builder()
			.callTimeout(timeout, TimeUnit.SECONDS)
			.connectTimeout(timeout, TimeUnit.SECONDS)
			.readTimeout(timeout, TimeUnit.SECONDS)
			.writeTimeout(timeout, TimeUnit.SECONDS)
			.addInterceptor(loggingInterceptor)
			.build()

		return Retrofit.Builder()
			.client(client)
			.baseUrl(baseUrl)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}


	fun crashMonitorExceptionHandler() = object : INetworkExceptionHandler {
		override fun handleException(throwable: Throwable) {
			CrashMonitor.trackWarning(throwable)
		}
	}

	fun uiExceptionHandler(context: Context) = object : INetworkExceptionHandler {
		override fun handleException(throwable: Throwable) {
			if (throwable is SocketTimeoutException) {
				val text = context.getText(R.string.fact_socket_error)
				Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
			} else {
				CrashMonitor.trackWarning(throwable)
				Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
			}
		}
	}


}