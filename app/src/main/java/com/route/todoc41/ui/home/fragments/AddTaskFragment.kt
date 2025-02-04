package com.route.todoc41.ui.home.fragments

import android.os.Bundle
import android.provider.CalendarContract.Calendars
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.route.todoc41.R
import com.route.todoc41.database.MyDatabase
import com.route.todoc41.database.dao.TasksDao
import com.route.todoc41.database.entity.Task
import com.route.todoc41.databinding.ActivityHomeBinding
import com.route.todoc41.databinding.FragmentAddTaskBinding
import com.route.todoc41.ui.util.clearDate
import com.route.todoc41.ui.util.clearSeconds
import com.route.todoc41.ui.util.clearTime
import com.route.todoc41.ui.util.getFormattedTime
import com.route.todoc41.ui.util.showDatePickerDialog
import com.route.todoc41.ui.util.showTimePickerDialog
import java.util.Calendar

class AddTaskFragment:BottomSheetDialogFragment() {
    lateinit var binding: FragmentAddTaskBinding
    lateinit var dao: TasksDao
    private var dateCalendar = Calendar.getInstance()
    private var timeCalendar = Calendar.getInstance()
    var onTaskAdded:OnTaskAdded?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dao = MyDatabase.getInstance().tasksDao()
        onSelectDateClick()
        onSelectTimeClick()
        onAddTaskClick()
    }


    private fun onSelectTimeClick() {
        binding.selectTimeTv.setOnClickListener {
            val calendar = Calendar.getInstance()
            showTimePickerDialog(calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),"Select Time:",childFragmentManager){hour,minute->
                binding.selectTimeTv.text = getFormattedTime(hour,minute)
                timeCalendar.set(Calendar.HOUR,hour)
                timeCalendar.set(Calendar.MINUTE,minute)
                timeCalendar.clearDate()
                timeCalendar.clearSeconds()

            }
        }
    }

    private fun onSelectDateClick() {
        binding.selectDateTv.setOnClickListener {
            showDatePickerDialog(requireContext()) { date, calendar ->
                binding.selectDateTv.text = date
                dateCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                dateCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                dateCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
                dateCalendar.clearTime()
            }
        }
    }
    private fun onAddTaskClick() {
        binding.addTaskBtn.setOnClickListener {

            if (!validateInput())
                return@setOnClickListener

            val task = createTask()
            dao.insertNewTask(task)
            onTaskAdded?.onAddTask(task)
            dismiss()
        }
    }

    private fun createTask():Task {
       return Task(title = binding.title.text.toString(), date = dateCalendar.timeInMillis, time =
       timeCalendar.timeInMillis, description = binding.description.text.toString())
    }

    fun validateInput():Boolean{
        var isValid = true
        if (binding.title.text.isNullOrBlank()){
            isValid = false
            binding.titleTil.error = getString(R.string.required_field)
        }
        if (binding.selectDateTv.text.isNullOrBlank()){
            isValid = false
            binding.selectDateTil.error = getString(R.string.required_field)
        }
        if (binding.selectTimeTv.text.isNullOrBlank()){
            isValid = false
            binding.selectTimeTil.error = getString(R.string.required_field)
        }
        return isValid
    }

    fun interface OnTaskAdded{
        fun onAddTask(task: Task)
    }
}