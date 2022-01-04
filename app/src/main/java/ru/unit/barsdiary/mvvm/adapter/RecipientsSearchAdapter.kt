package ru.unit.barsdiary.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.unit.barsdiary.databinding.RecyclerItemFoundUserBinding
import ru.unit.barsdiary.domain.global.pojo.FoundUserPojo

class RecipientsSearchAdapter : PagingDataAdapter<FoundUserPojo, RecipientsSearchAdapter.ViewHolder>(object : DiffUtil.ItemCallback<FoundUserPojo>() {
    override fun areItemsTheSame(oldItem: FoundUserPojo, newItem: FoundUserPojo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FoundUserPojo, newItem: FoundUserPojo): Boolean {
        return oldItem == newItem
    }
}) {

    var clickableListener: (id: FoundUserPojo) -> Unit = {}

    class ViewHolder(val binding: RecyclerItemFoundUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemFoundUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return

        holder.binding.textViewClass.isVisible = item.className != null
        holder.binding.textViewInfo.isVisible = item.childFullName != null

        holder.binding.textViewName.text = item.fullName
        holder.binding.textViewClass.text = item.className
        holder.binding.textViewInfo.text = item.childFullName

        holder.binding.root.setOnClickListener {
            clickableListener.invoke(item)
        }
    }
}