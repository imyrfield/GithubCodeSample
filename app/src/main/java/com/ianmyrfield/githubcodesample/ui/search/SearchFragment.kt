package com.ianmyrfield.githubcodesample.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.ianmyrfield.githubcodesample.data.models.GithubUser
import com.ianmyrfield.githubcodesample.R
import com.ianmyrfield.githubcodesample.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment: Fragment(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModel()
    private val repoAdapter: RepositoryListAdapter by lazy { RepositoryListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSearchBinding.bind(view)
        setupViews(binding)
        setupObservables(binding)
    }

    private fun setupViews(binding: FragmentSearchBinding?) {
        binding ?: return

        binding.searchET.setOnEditorActionListener { _, actionId, event ->
            if (actionId == IME_ACTION_SEARCH && event?.action == KeyEvent.ACTION_DOWN) {
                onSearchClicked(binding)
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }

        binding.searchBtn.setOnClickListener { onSearchClicked(binding) }

        binding.repositoryList.adapter = repoAdapter
    }

    private fun onSearchClicked(binding: FragmentSearchBinding) {
        hideKeyboard()

        val userId = binding.searchInput.editText?.text?.toString()
        viewModel.searchUser(userId)
    }

    private fun setupObservables(binding: FragmentSearchBinding?) {
        binding ?: return

        viewModel.userInfo.observe(viewLifecycleOwner) { userInfo -> setUserInfo(binding, userInfo) }
        viewModel.userRepos.observe(viewLifecycleOwner, repoAdapter::submitList)
        viewModel.errorMsg.observe(viewLifecycleOwner) { error -> showError(binding, error)}
    }

    private fun setUserInfo(binding: FragmentSearchBinding, userInfo: GithubUser?) {
        binding.userName.isVisible = userInfo != null
        binding.userName.text = userInfo?.name

        binding.userAvatar.isVisible = userInfo != null
        Glide.with(this)
            .load(userInfo?.avatarUrl)
            // TODO: Fix animation fade/translation
            .transition(GenericTransitionOptions.with(R.anim.slide_in))
            .error(R.drawable.baseline_person_24)
            .into(binding.userAvatar)
    }

    // TODO: Implement more flexible error view handling
    private fun showError(binding: FragmentSearchBinding, error: SearchError?) {
        binding.errorView.isVisible = error != null
        error?.let { binding.errorView.text = getString(error.message) }
    }

    private fun hideKeyboard() {
        val context = context ?: return
        val imm = getSystemService(context, InputMethodManager::class.java) ?: return
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}