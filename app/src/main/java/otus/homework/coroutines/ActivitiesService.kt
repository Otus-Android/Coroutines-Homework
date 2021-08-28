package otus.homework.coroutines

import retrofit2.http.GET

interface ActivitiesService {

    @GET("/api/activity/")
    suspend fun getActivity() : ActivityResponse
}