package ru.unit.barsdiary.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.RecyclerItemServerBinding
import ru.unit.barsdiary.domain.auth.pojo.ServerInfoPojo

class ServerListAdapter(
    private val list: List<ServerInfoPojo>,
    private val serverClickListener: (url: String) -> Unit,
) : RecyclerView.Adapter<ServerListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RecyclerItemServerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(RecyclerItemServerBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == list.size) { // custom url
            holder.binding.textViewServerName.text = holder.binding.root.context.getString(R.string.custom_url)
            holder.binding.root.setOnClickListener {
                serverClickListener.invoke("")
            }
        } else {
            holder.binding.textViewServerName.text = list[position].name
            holder.binding.root.setOnClickListener {
                serverClickListener.invoke(list[position].url)
            }
        }
    }

    override fun getItemCount(): Int = list.size + 1
}