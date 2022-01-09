package ru.unit.barsdiary.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.unit.barsdiary.databinding.RecyclerItemAdvertBoardBinding
import ru.unit.barsdiary.domain.global.pojo.AdvertBoardItemPojo

class AdvertBoardAdapter(
    private val advertBoard: List<AdvertBoardItemPojo>,
    private val dateFormatter: (date: String?) -> String?,
    private val itemClickListener: (item: AdvertBoardItemPojo) -> Unit
) : RecyclerView.Adapter<AdvertBoardAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RecyclerItemAdvertBoardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemAdvertBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = advertBoard[position]
        holder.binding.titleTextView.text = data.theme

        holder.binding.authorTextView.text = data.author

        holder.binding.schoolTextView.isVisible = !data.school.isNullOrBlank()
        holder.binding.schoolTextView.text = data.school

        val date = dateFormatter.invoke(data.advertDate)
        if (date.isNullOrEmpty()) {
            holder.binding.dateTextView.isVisible = false
        } else {
            holder.binding.dateTextView.isVisible = true
            holder.binding.dateTextView.text = date
        }

        holder.binding.hasFileImageView.isVisible = !data.fileUrl.isNullOrEmpty()

        holder.binding.root.setOnClickListener {
            itemClickListener.invoke(data)
        }
    }

    override fun getItemCount(): Int = advertBoard.size
}