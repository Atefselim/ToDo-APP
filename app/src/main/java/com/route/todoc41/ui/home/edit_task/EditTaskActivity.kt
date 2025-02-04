package com.route.todoc41.ui.home.edit_task

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.route.todoc41.database.MyDatabase
import com.route.todoc41.database.dao.TasksDao
import com.route.todoc41.database.entity.Task
import com.route.todoc41.databinding.ActivityEditTaskBinding
import com.route.todoc41.model.AppConstant
import com.route.todoc41.ui.util.clearDate
import com.route.todoc41.ui.util.clearSeconds
import com.route.todoc41.ui.util.clearTime
import com.route.todoc41.ui.util.getFormattedTime
import com.route.todoc41.ui.util.showDatePickerDialog
import com.route.todoc41.ui.util.showTimePickerDialog
import java.util.Calendar


class EditTaskActivity : AppCompatActivity() {
    lateinit var dao: TasksDao
    lateinit var binding: ActivityEditTaskBinding
    private var dateCalendar = Calendar.getInstance()
    private var timeCalendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dao = MyDatabase.getInstance().tasksDao()
        val task: Task? = intent.getParcelableExtra(AppConstant.EXTRA_TASK)

        task?.let {
            binding.taskTitleEditText.setText(it.title)
            binding.taskDescriptionEditText.setText(it.description)
            dateCalendar.timeInMillis = it.date
            timeCalendar.timeInMillis = it.time
        }
        binding.selectTimeTxtView.setOnClickListener {
            onSelectTimeClick()
        }
        binding.dateSelectedTv.setOnClickListener {
            onSelectDateClick()
        }
        binding.icBackImg.setOnClickListener {
            finish()
        }
        binding.saveChangesBtn.setOnClickListener {
            if (!dataValid()) {
                return@setOnClickListener
            }
            val updatedTask = creatTask(task)
            dao.updateTask(updatedTask)

            val resultIntent = Intent().apply {
                putExtra(AppConstant.EXTRA_TASK, updatedTask)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

    }

    private fun creatTask(task: Task?):Task {
        return task?.apply {
            title = binding.taskTitleEditText.text.toString()
            description = binding.taskDescriptionEditText.text.toString()
            date = dateCalendar.timeInMillis
            time = timeCalendar.timeInMillis
        } ?: Task(
            title = binding.taskTitleEditText.text.toString(),
            description = binding.taskDescriptionEditText.text.toString(),
            date = dateCalendar.timeInMillis,
            time = timeCalendar.timeInMillis
        )    }

    fun dataValid():Boolean{
        var isValid = true
        if (binding.taskTitleEditText.text.isBlank() || binding.taskTitleEditText.text.isBlank()){
            isValid = false
            binding.taskTitleEditText.error = "Required"
        }
        if (binding.taskDescriptionEditText.text.isBlank() || binding.taskTitleEditText.text.isBlank()){
            isValid = false
            binding.taskDescriptionEditText.error = "Required"
        }
        if (binding.dateSelectedTv.text.isBlank() || binding.taskTitleEditText.text.isBlank()){
            isValid = false
            binding.dateSelectedTv.error = "Required"
        }
        if (binding.selectTimeTxtView.text.isBlank() || binding.taskTitleEditText.text.isBlank()){
            isValid = false
            binding.selectTimeTxtView.error = "Required"
        }
        return isValid
    }
    private fun onSelectDateClick() {
        binding.dateSelectedTv.setOnClickListener {
            showDatePickerDialog(this) { date, calendar ->
                binding.dateSelectedTv.text = date
                dateCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                dateCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                dateCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
                dateCalendar.clearTime()
            }
        }
    }
        private fun onSelectTimeClick() {
            binding.selectTimeTxtView.setOnClickListener {
                val calendar = Calendar.getInstance()
                showTimePickerDialog(
                    calendar.get(Calendar.HOUR),
                    calendar.get(Calendar.MINUTE),
                    "Select Time:",
                    supportFragmentManager
                ) { hour, minute ->
                    binding.selectTimeTxtView.text = getFormattedTime(hour, minute)
                    timeCalendar.set(Calendar.HOUR, hour)
                    timeCalendar.set(Calendar.MINUTE, minute)
                    timeCalendar.clearDate()
                    timeCalendar.clearSeconds()

                }
            }
    }



}