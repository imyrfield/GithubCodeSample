package com.ianmyrfield.githubcodesample.data

import com.ianmyrfield.githubcodesample.data.models.GithubRepo
import com.ianmyrfield.githubcodesample.data.models.GithubUser
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("users/{userId}")
    suspend fun getUserInfo(
        @Path("userId") userId: String
    ): GithubUser

    @GET("users/{userId}/repos")
    suspend fun getReposForUser(
        @Path("userId") userId: String,
        @Query("page") page: Int,
        @Query("per_page") pageCount: Int,
    ): List<GithubRepo>

}