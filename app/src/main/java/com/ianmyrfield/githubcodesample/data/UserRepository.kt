package com.ianmyrfield.githubcodesample.data

import com.ianmyrfield.githubcodesample.data.models.GithubRepo
import com.ianmyrfield.githubcodesample.data.models.GithubUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val api: GithubApi): UserDataSource {

    // This only works because it's queried after loading all the repos.
    // Ideally, this would get calculated when it's needed, and the value would be cached then.
    override var totalForks: Long = 0
        private set

    override suspend fun getUserInfo(userId: String): GithubUser = withContext(Dispatchers.IO) {
         api.getUserInfo(userId)
    }

    // Normally this would should be paginated, but the requirement to count the total number of
    // forks across all repos means we need to fetch all the repos at the start
    override suspend fun getReposForUser(userId: String): List<GithubRepo> = withContext(Dispatchers.IO) {
        totalForks = 0

        var page = 1
        val RESULTS_PER_PAGE = 100
        var moreResultsAvailable = true

        var result = arrayListOf<GithubRepo>()

        while (moreResultsAvailable) {
            val response = api.getReposForUser(userId = userId, page = page, pageCount = RESULTS_PER_PAGE)
            result.addAll(response)

            moreResultsAvailable = response.count() >= RESULTS_PER_PAGE
            page++
        }

        totalForks = result.sumOf { it.forks.toLong() }

        result
    }
}