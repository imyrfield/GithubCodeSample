package com.ianmyrfield.githubcodesample.ui.search

import androidx.annotation.StringRes
import com.ianmyrfield.githubcodesample.R

sealed class SearchError(@StringRes val message: Int) {
    object UserNotFound: SearchError(R.string.no_results_found)
    object NoInternet: SearchError(R.string.no_internet)
    object InvalidSearch: SearchError(R.string.invalid_search)
    object RateLimitExceeded: SearchError(R.string.rate_limit_exceeded)

}