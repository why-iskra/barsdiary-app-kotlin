package ru.unit.barsdiary.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.unit.barsdiary.databinding.RecyclerItemLessonBinding
import ru.unit.barsdiary.domain.diary.pojo.DiaryLessonPojo
import ru.unit.barsdiary.domain.diary.pojo.HomeworkLessonPojo

class LessonsAdapter(
    private val lessons: List<DiaryLessonPojo>,
    private val homeworks: List<HomeworkLessonPojo>,
    private val lessonClickListener: (lesson: DiaryLessonPojo, homework: HomeworkLessonPojo?) -> Unit,
) : RecyclerView.Adapter<LessonsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RecyclerItemLessonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecyclerItemLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = lessons[position]
        val homework = if (homeworks.size <= position) null else homeworks[position]

        val timeBegin = lesson.timeBegin?.substringBeforeLast(':')
        val timeEnd = lesson.timeEnd?.substringBeforeLast(':')

        holder.binding.textViewDiscipline.text = lesson.discipline
        if (timeBegin.isNullOrBlank() || timeEnd.isNullOrBlank()) {
            holder.binding.textViewTime.text = ""
        } else {
            holder.binding.textViewTime.text = "$timeBegin - $timeEnd"
        }
        holder.binding.textViewOffice.text = lesson.office
        holder.binding.textViewMark.text = lesson.mark

        holder.binding.attendanceView.visibility = if (lesson.attendance.isNullOrBlank()) View.INVISIBLE else View.VISIBLE

        holder.binding.root.setOnClickListener {
            lessonClickListener.invoke(lesson, homework)
        }
    }

    override fun getItemCount(): Int = lessons.size
}