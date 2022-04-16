package otus.homework.coroutines

import okhttp3.*

class MockInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (request.url().host() == "cat-fact.herokuapp.com") {
            val contentType = "application/json"
            val body = "{\"text\": \"Cats have nine lives\"}"

            return Response.Builder()
                .request(request)
                .message(body)
                .protocol(Protocol.HTTP_1_0)
                .code(200)
                .header("content-type", contentType)
                .body(ResponseBody.create(MediaType.get(contentType), body))
                .build()
        }

        return chain.proceed(request)
    }
}