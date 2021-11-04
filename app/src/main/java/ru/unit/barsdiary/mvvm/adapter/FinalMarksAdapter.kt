package ru.unit.barsdiary.mvvm.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.unit.barsdiary.databinding.RecyclerItemFinalMarkBinding
import ru.unit.barsdiary.domain.person.pojo.TotalMarksDisciplinePojo

class FinalMarksAdapter(val list: List<TotalMarksDisciplinePojo>) : RecyclerView.Adapter<FinalMarksAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RecyclerItemFinalMarkBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemFinalMarkBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.binding.textViewName.text = data.discipline
        holder.binding.textViewMark.text = data.periodMarks.map { it.mark }.joinToString()
    }

    override fun getItemCount(): Int = list.size
}