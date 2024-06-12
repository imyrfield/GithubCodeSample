package com.ianmyrfield.githubcodesample.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class GithubRepo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("updated_at") val lastUpdated: String?,
    @SerializedName("stargazers_count") val numStargazers: Int,
    @SerializedName("forks") val forks: Int
): Parcelable