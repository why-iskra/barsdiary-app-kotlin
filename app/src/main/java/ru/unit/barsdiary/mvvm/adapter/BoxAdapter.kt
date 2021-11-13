package ru.unit.barsdiary.mvvm.adapter

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.unit.barsdiary.R
import ru.unit.barsdiary.data.di.annotation.MailDateFormatter
import ru.unit.barsdiary.databinding.RecyclerItemMailBinding
import ru.unit.barsdiary.domain.global.pojo.MessagePojo
import ru.unit.barsdiary.other.HtmlUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class BoxAdapter @Inject constructor(
    @MailDateFormatter private val mailDateFormatter: DateTimeFormatter,
) : PagingDataAdapter<MessagePojo, BoxAdapter.ViewHolder>(object : DiffUtil.ItemCallback<MessagePojo>() {
    override fun areItemsTheSame(oldItem: MessagePojo, newItem: MessagePojo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MessagePojo, newItem: MessagePojo): Boolean {
        return oldItem == newItem
    }
}) {
    var isInBox = true

    private val selected = mutableListOf<Int>()

    private var clickListener: (data: MessagePojo) -> Unit = {}
    private var selectListener: (selected: MutableList<Int>) -> Unit = {}

    inner class ViewHolder(val binding: RecyclerItemMailBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemMailBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return

        holder.binding.titleTextView.text = item.subject

        holder.binding.authorTextView.text = if (isInBox) item.senderFullName else item.receivers.joinToString { it.fullname }

        holder.binding.contentTextView.text = HtmlUtils.convert(item.shortText)

        holder.binding.dateTextView.text = dateToString(item.date)

        if (item.attachments.isEmpty()) {
            holder.binding.attachmentsTextView.visibility = View.GONE
        } else {
            holder.binding.attachmentsTextView.visibility = View.VISIBLE
            holder.binding.attachmentsTextView.text = Html.fromHtml(item.attachments.size.toString(), Html.FROM_HTML_MODE_COMPACT)
        }

        holder.binding.imageView.setImageDrawable(ContextCompat.getDrawable(holder.binding.root.context, getIcon(item)))

        holder.binding.root.setOnClickListener {
            if (hasSelected()) {
                if (isSelected(item.id)) {
                    selected.remove(item.id)
                } else {
                    selected.add(item.id)
                }

                holder.binding.imageView.setImageDrawable(ContextCompat.getDrawable(holder.binding.root.context, getIcon(item)))
                selectListener.invoke(selected)
            } else {
                if (isInBox) {
                    item.markRead = true
                }

                clickListener.invoke(item)
            }
        }

        holder.binding.root.setOnLongClickListener {
            if (!isSelected(item.id)) {
                selected.add(item.id)
                holder.binding.imageView.setImageDrawable(ContextCompat.getDrawable(holder.binding.root.context, getIcon(item)))
                selectListener.invoke(selected)
            }

            return@setOnLongClickListener true
        }
    }

    private fun getIcon(item: MessagePojo) = if (isSelected(item.id)) {
        R.drawable.ic_round_check_circle_outline_24
    } else if (item.isNew && (!item.markRead && isInBox)) {
        R.drawable.ic_round_mail_unread_24
    } else {
        R.drawable.ic_round_mail_24
    }

    private fun isSelected(id: Int) = selected.contains(id)

    fun hasSelected() = selected.isNotEmpty()

    fun selectAll() {
        (0 until itemCount).forEach {
            val item = getItem(it) ?: return@forEach
            notifyItemChanged(it)
            selected.add(item.id)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun resetSelected() {
        selected.clear()
        notifyDataSetChanged()
        selectListener.invoke(selected)
    }

    fun setMailOnClick(listener: (data: MessagePojo) -> Unit) {
        this.clickListener = listener
    }

    fun setOnSelect(listener: (selected: MutableList<Int>) -> Unit) {
        this.selectListener = listener
    }

    private fun dateToString(date: String?): String? {
        date ?: return null

        runCatching {
            val current = LocalDateTime.now()
            val dateTime = LocalDateTime.parse(date, mailDateFormatter)
            return when {
                current.year - dateTime.year > 0 -> DateTimeFormatter.ofPattern("yyyy").format(dateTime)
                current.dayOfYear - dateTime.dayOfYear > 0 -> DateTimeFormatter.ofPattern("dd MMM").format(dateTime)
                else -> DateTimeFormatter.ofPattern("HH:mm").format(dateTime)
            }
        }

        return null
    }
}