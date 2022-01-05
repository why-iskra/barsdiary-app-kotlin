package ru.unit.barsdiary.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.unit.barsdiary.databinding.RecyclerItemDisciplineMarkBinding
import ru.unit.barsdiary.domain.mark.pojo.DisciplineMarksPojo

class MarksAdapter(
    private val disciplineMarks: List<DisciplineMarksPojo>,
    private val disciplineClickListener: (position: Int) -> Unit,
) : RecyclerView.Adapter<MarksAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RecyclerItemDisciplineMarkBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemDisciplineMarkBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val discipline = disciplineMarks[position]
        holder.binding.textViewDiscipline.text = discipline.discipline
        holder.binding.textViewAverageGrade.text = discipline.averageMark
        holder.binding.root.setOnClickListener {
            disciplineClickListener.invoke(position)
        }
    }

    override fun getItemCount(): Int = disciplineMarks.size
}