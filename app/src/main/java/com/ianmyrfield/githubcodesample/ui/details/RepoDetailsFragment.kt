package com.ianmyrfield.githubcodesample.ui.details

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ianmyrfield.githubcodesample.R
import com.ianmyrfield.githubcodesample.data.UserDataSource
import com.ianmyrfield.githubcodesample.data.models.GithubRepo
import com.ianmyrfield.githubcodesample.databinding.FragmentRepoDetailsBinding
import org.koin.android.ext.android.inject

class RepoDetailsFragment: Fragment(R.layout.fragment_repo_details) {

    private val args: RepoDetailsFragmentArgs by navArgs()
    private val repository: UserDataSource by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentRepoDetailsBinding.bind(view)
        setData(binding, args.repo)
    }

    private fun setData(binding: FragmentRepoDetailsBinding, repo: GithubRepo) {
        binding.repoName.text = repo.name
        binding.repoDescription.text = repo.description
        binding.repoUpdatedDate.text = getString(
            R.string.last_updated_formatted,
            DateUtils.formatDate(repo.lastUpdated)
        )
        binding.repoStargazers.text = repo.numStargazers.toString()
        binding.repoForks.text = repo.forks.toString()

        setTotalForks(binding)
    }

    private fun setTotalForks(binding: FragmentRepoDetailsBinding) {
        val totalForks = repository.totalForks

        binding.totalForks.text = getString(R.string.total_forks_formatted, totalForks)
        // TODO: Logic should be moved to a ViewModel so it can be tested
        if (totalForks > MIN_FORKS) {
            binding.totalForks.setTextColor(ContextCompat.getColor(binding.totalForks.context, R.color.fork_highlight))
        }
    }

    companion object {
        const val MIN_FORKS = 5_000L
    }
}