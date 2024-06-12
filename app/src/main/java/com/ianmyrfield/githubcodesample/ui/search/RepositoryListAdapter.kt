package com.ianmyrfield.githubcodesample.ui.search

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ianmyrfield.githubcodesample.data.models.GithubRepo

class RepositoryListAdapter: ListAdapter<GithubRepo, RepoViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {

        private val diffCallback = object : DiffUtil.ItemCallback<GithubRepo>() {

            override fun areItemsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.description == newItem.description &&
                        oldItem.forks == newItem.forks &&
                        oldItem.lastUpdated == newItem.lastUpdated &&
                        oldItem.numStargazers == newItem.numStargazers
            }
        }
    }
}