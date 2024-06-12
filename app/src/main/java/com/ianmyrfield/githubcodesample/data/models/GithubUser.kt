package com.ianmyrfield.githubcodesample.data.models

import com.google.gson.annotations.SerializedName

data class GithubUser(
    @SerializedName("login") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("avatar_url") val avatarUrl: String,
)
