package com.ianmyrfield.githubcodesample.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ianmyrfield.githubcodesample.data.models.GithubRepo
import com.ianmyrfield.githubcodesample.databinding.RepoListItemBinding

class RepoViewHolder(
    private val binding: RepoListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(repo: GithubRepo) {
        binding.repoName.text = repo.name
        binding.repoDescription.text = repo.description

        binding.root.setOnClickListener {
            val action = SearchFragmentDirections.navigateToRepoDetailsFragment(repo)
            binding.root.findNavController().navigate(action)
        }
    }

    companion object {

        fun create(parent: ViewGroup): RepoViewHolder {
            val binding = RepoListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return RepoViewHolder(binding)
        }
    }
}