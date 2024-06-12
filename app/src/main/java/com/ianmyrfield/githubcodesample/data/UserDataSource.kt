package com.ianmyrfield.githubcodesample.data

import com.ianmyrfield.githubcodesample.data.models.GithubRepo
import com.ianmyrfield.githubcodesample.data.models.GithubUser

interface UserDataSource {

    val totalForks: Long

    suspend fun getUserInfo(userId: String): GithubUser

    suspend fun getReposForUser(userId: String): List<GithubRepo>
}