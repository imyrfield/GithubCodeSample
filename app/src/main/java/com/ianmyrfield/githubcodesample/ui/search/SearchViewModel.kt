package com.ianmyrfield.githubcodesample.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianmyrfield.githubcodesample.data.models.GithubUser
import com.ianmyrfield.githubcodesample.data.UserDataSource
import com.ianmyrfield.githubcodesample.data.models.GithubRepo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class SearchViewModel(private val repo: UserDataSource): ViewModel() {

    private val _userInfo: MutableLiveData<GithubUser?> = MutableLiveData()
    val userInfo: LiveData<GithubUser?> = _userInfo

    private val _userRepos: MutableLiveData<List<GithubRepo>> = MutableLiveData()
    val userRepos: LiveData<List<GithubRepo>> = _userRepos

    private val _errorMsg: MutableLiveData<SearchError?> = MutableLiveData()
    val errorMsg: LiveData<SearchError?> = _errorMsg

    fun searchUser(userId: String?) {
        // Don't re-search for the same user
        if (userId == userInfo.value?.id) return

        // Clear previous values
        _userInfo.value = null
        _userRepos.value = emptyList()
        _errorMsg.value = null

        if (userId.isNullOrBlank()) {
            // TODO: Would be better as an error message on the EditText itself
            _errorMsg.value = SearchError.InvalidSearch
            return
        }

        // Normally we should display a loading view to indicate the app is processing the users
        // input, but the video didn't show one, so I didn't implement one
        getUser(userId)
        getRepos(userId)
    }

    private fun getUser(userId: String) {
        viewModelScope.launch(exceptionHandler) {
            val response = repo.getUserInfo(userId)

            _userInfo.postValue(response)
        }
    }

    private fun getRepos(userId: String) {
        viewModelScope.launch(exceptionHandler) {
            val repos = repo.getReposForUser(userId)
            _userRepos.postValue(repos)

            // TODO: Handle no results returned, show message to user
        }
    }

    // This is very basic exception handling, but it could be much more robust
    // It should also be handled elsewhere, as http error codes shouldn't be interpreted in the view model
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            // No Internet
            is UnknownHostException -> {
                _errorMsg.value = SearchError.NoInternet
            }

            is HttpException -> {
                // User not found
                if (throwable.code() == 404) {
                    _errorMsg.value = SearchError.UserNotFound
                }

                if (throwable.code() == 403) {
                    _errorMsg.value = SearchError.RateLimitExceeded
                }
            }
        }
    }
}