package ru.unit.barsdiary.mvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.unit.barsdiary.databinding.RecyclerItemBirthdayBinding
import ru.unit.barsdiary.domain.global.pojo.BirthdayPojo

class BirthdaysAdapter(
    private val birthdays: List<BirthdayPojo>,
    private val dateFormatter: (date: String) -> String,
) : RecyclerView.Adapter<BirthdaysAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RecyclerItemBirthdayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemBirthdayBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = birthdays[position]
        holder.binding.textViewName.text = data.shortName
        holder.binding.textViewDate.text = dateFormatter.invoke(data.date)
    }

    override fun getItemCount(): Int = birthdays.size
}