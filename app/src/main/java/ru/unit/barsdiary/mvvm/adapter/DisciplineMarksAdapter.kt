package ru.unit.barsdiary.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.unit.barsdiary.databinding.RecyclerItemMarkBinding
import ru.unit.barsdiary.domain.mark.pojo.MarkPojo

class DisciplineMarksAdapter(
    private val marks: List<MarkPojo>,
) : RecyclerView.Adapter<DisciplineMarksAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RecyclerItemMarkBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemMarkBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mark = marks[position]
        holder.binding.textViewDate.text = mark.date
        holder.binding.textViewComment.text = mark.description
        holder.binding.textViewMark.text = mark.mark
    }

    override fun getItemCount(): Int = marks.size
}