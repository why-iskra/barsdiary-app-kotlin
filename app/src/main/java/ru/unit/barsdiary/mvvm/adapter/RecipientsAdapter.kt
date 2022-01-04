package ru.unit.barsdiary.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.unit.barsdiary.databinding.RecyclerItemRecipientBinding
import ru.unit.barsdiary.domain.global.pojo.FoundUserPojo

class RecipientsAdapter(
    private val list: List<FoundUserPojo>,
    private val clickListener: (item: FoundUserPojo) -> Unit,
    ) : RecyclerView.Adapter<RecipientsAdapter.ViewHolder>() {

    class ViewHolder(val binding: RecyclerItemRecipientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemRecipientBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.binding.chip.text = item.fullName
        holder.binding.chip.setOnClickListener {
            clickListener.invoke(item)
        }
    }

    override fun getItemCount(): Int = list.size
}