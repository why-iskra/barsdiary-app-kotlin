package ru.unit.barsdiary.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import ru.unit.barsdiary.databinding.RecyclerItemLoadStateBinding

class LoadStateAdapter(
    private val retryClickListener: View.OnClickListener,
) : androidx.paging.LoadStateAdapter<LoadStateAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RecyclerItemLoadStateBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(RecyclerItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        when (loadState) {
            is LoadState.NotLoading -> {
                holder.binding.progressBar.visibility = View.GONE
                holder.binding.retryButton.visibility = View.GONE
            }
            is LoadState.Loading -> {
                holder.binding.progressBar.visibility = View.VISIBLE
                holder.binding.retryButton.visibility = View.GONE
            }
            is LoadState.Error -> {
                holder.binding.progressBar.visibility = View.GONE
                holder.binding.retryButton.visibility = View.VISIBLE
            }
        }

        holder.binding.retryButton.setOnClickListener(retryClickListener)
    }

}