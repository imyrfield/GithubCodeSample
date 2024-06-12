package com.ianmyrfield.githubcodesample

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ianmyrfield.githubcodesample.data.UserDataSource
import com.ianmyrfield.githubcodesample.data.models.GithubRepo
import com.ianmyrfield.githubcodesample.data.models.GithubUser
import com.ianmyrfield.githubcodesample.ui.search.SearchError
import com.ianmyrfield.githubcodesample.ui.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

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
    fun `SearchError#InvalidSearch is returned when search field is empty`() = runTest {

        val fakeDataSource = object : UserDataSource {

            override val totalForks: Long = 0

            override suspend fun getUserInfo(userId: String): GithubUser {
                return GithubUser("", "", "")
            }

            override suspend fun getReposForUser(userId: String): List<GithubRepo> {
                return listOf()
            }
        }

        val viewModel = SearchViewModel(fakeDataSource)

        viewModel.searchUser("")

        assertEquals(SearchError.InvalidSearch, viewModel.errorMsg.value)
    }

    // TODO: Test that the other types of SearchErrors get handled properly
}
