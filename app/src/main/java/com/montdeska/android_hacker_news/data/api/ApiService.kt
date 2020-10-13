package com.montdeska.android_hacker_news.data.api

import com.montdeska.android_hacker_news.data.local.models.Stories
import retrofit2.http.*

interface ApiService {
    @GET("api/v1/search_by_date?query=android&hitsPerPage=100")
    suspend fun getStories(
        @HeaderMap headers: HashMap<String, String>
    ): Stories?
}