package com.ianmyrfield.githubcodesample

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ianmyrfield.githubcodesample.data.GithubApi
import com.ianmyrfield.githubcodesample.data.UserRepository
import com.ianmyrfield.githubcodesample.data.models.GithubRepo
import com.ianmyrfield.githubcodesample.data.models.GithubUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun before() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `totalForks equals sum of all forks`() = runTest {

        val api = object: GithubApi {

            override suspend fun getUserInfo(userId: String): GithubUser {
                return GithubUser("", "", "")
            }

            override suspend fun getReposForUser(
                userId: String,
                page: Int,
                pageCount: Int
            ): List<GithubRepo> {
                return listOf(
                    GithubRepo(1, "", "", "", 0, 12),
                    GithubRepo(2, "", "", "", 0, 15)
                )
            }
        }

        val userRepository = UserRepository(api)

        userRepository.getReposForUser("")

        assertEquals(27, userRepository.totalForks)
    }
}