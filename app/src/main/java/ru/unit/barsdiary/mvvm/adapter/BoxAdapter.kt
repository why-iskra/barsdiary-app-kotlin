package ru.unit.barsdiary.mvvm.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.RecyclerItemMailBinding
import ru.unit.barsdiary.domain.global.pojo.MessagePojo
import ru.unit.barsdiary.sdk.di.annotation.MailDateFormatter
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

    private var listener: (data: MessagePojo) -> Unit = {}

    inner class ViewHolder(val binding: RecyclerItemMailBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerItemMailBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return

        holder.binding.titleTextView.text = item.subject

        holder.binding.authorTextView.text = if (isInBox) item.senderFullName else item.receivers.joinToString { it.fullname }

        holder.binding.contentTextView.text = item.shortText

        holder.binding.dateTextView.text = dateToString(item.date)

        if (item.attachments.isEmpty()) {
            holder.binding.attachmentsTextView.visibility = View.GONE
        } else {
            holder.binding.attachmentsTextView.visibility = View.VISIBLE
            holder.binding.attachmentsTextView.text = Html.fromHtml(item.attachments.size.toString(), Html.FROM_HTML_MODE_COMPACT)
        }

        holder.binding.imageView.setImageDrawable(
            ContextCompat.getDrawable(
                holder.binding.root.context,
                if (item.isNew && (!item.markRead && isInBox)) R.drawable.ic_round_mail_unread_24 else R.drawable.ic_round_mail_24
            )
        )

        holder.binding.root.setOnClickListener {
            if (isInBox) {
                item.markRead = true
            }
            listener.invoke(item)
        }
    }

    fun setMailOnClick(listener: (data: MessagePojo) -> Unit) {
        this.listener = listener
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