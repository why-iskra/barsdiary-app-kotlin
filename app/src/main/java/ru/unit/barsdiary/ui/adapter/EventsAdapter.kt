package ru.unit.barsdiary.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.unit.barsdiary.databinding.RecyclerItemEventBinding
import ru.unit.barsdiary.domain.global.pojo.EventPojo

class EventsAdapter(
    private val events: List<EventPojo>,
    private val dateFormatter: (date: String?) -> String?,
) : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RecyclerItemEventBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecyclerItemEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]

        val dateRaw = event.date
        val dateStr = event.dateStr
        val theme = event.theme

        if (
            theme.isNullOrBlank()
            && dateStr.isNullOrBlank()
            && dateRaw.isNullOrBlank()
        ) {
            holder.binding.root.isVisible = false
        } else {
            holder.binding.root.isVisible = true

            holder.binding.themeTextView.isVisible = !theme.isNullOrBlank()
            holder.binding.themeTextView.text = theme

            if (dateStr.isNullOrBlank()) {
                holder.binding.dateTextView.isVisible = true
                holder.binding.dateFullTextView.isVisible = false

                val date = dateFormatter(dateRaw)
                if (date.isNullOrEmpty()) {
                    holder.binding.dateTextView.isVisible = false
                } else {
                    holder.binding.dateTextView.isVisible = true
                    holder.binding.dateTextView.text = date
                }
            } else {
                holder.binding.dateTextView.isVisible = false
                holder.binding.dateFullTextView.isVisible = true

                holder.binding.dateFullTextView.text = dateStr
            }
        }
    }

    override fun getItemCount(): Int = events.size
}