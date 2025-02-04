package com.route.todoc41.ui.home.fragments.tasks_fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.islamiapp.adapters.callbacks.OnTaskClickListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.route.todoc41.database.MyDatabase
import com.route.todoc41.database.dao.TasksDao
import com.route.todoc41.database.entity.Task
import com.route.todoc41.databinding.FragmentTasksBinding
import com.route.todoc41.model.AppConstant
import com.route.todoc41.ui.home.edit_task.EditTaskActivity
import com.route.todoc41.ui.home.fragments.callbacks.OnBtnDeleteClickListener
import com.route.todoc41.ui.home.fragments.callbacks.OnBtnDoneClickListener
import com.route.todoc41.ui.util.clearTime
import java.util.Calendar

class TasksFragment:Fragment() {
lateinit var binding: FragmentTasksBinding
private val adapter = TasksAdapter()
    private lateinit var editTaskLauncher: ActivityResultLauncher<Intent>

    private lateinit var dao: TasksDao
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TasksFragment", "onCreate: Registering ActivityResultLauncher")

        editTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("TasksFragment", "ActivityResultLauncher: Received result")
            if (result.resultCode == Activity.RESULT_OK) {
                val updatedTask = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(AppConstant.EXTRA_TASK, Task::class.java)
                } else {
                    result.data?.getParcelableExtra(AppConstant.EXTRA_TASK)
                }

                updatedTask?.let {
                    Log.d("TasksFragment", "Updated task received: $updatedTask")
                    loadAllTasksOfDate(getSelectedDate().timeInMillis)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        Log.d("TasksFragment", "onCreate: ActivityResultLauncher registered successfully")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dao = MyDatabase.getInstance().tasksDao()
        initRecyclerView()
        initCalendarView()

        

    }


    private fun initCalendarView() {
        binding.calendarView.selectedDate = CalendarDay.today()
        binding.calendarView.setOnDateChangedListener { _, date, selected ->
            val calendar = Calendar.getInstance()//current time
            calendar.set(Calendar.YEAR,date.year)
            calendar.set(Calendar.MONTH,date.month-1)
            calendar.set(Calendar.DAY_OF_MONTH,date.day)
            calendar.clearTime()
            if (selected){
                val tasks = dao.getAllTasksByDate(calendar.timeInMillis).toMutableList()
                Log.e("TAG", "initCalendarView: $tasks", )
                adapter.setTasksList(tasks)
            }
        }
    }


    private fun initRecyclerView() {
        binding.rvTasks.adapter = adapter
        adapter.onTaskClickListener = object : OnTaskClickListener {
            override fun onTaskClick(task: Task, position: Int) {
                Log.d("TasksFragment", "Launching EditTaskActivity")
                val intent = Intent(activity, EditTaskActivity::class.java).apply {
                    putExtra(AppConstant.EXTRA_TASK, task)
                }
                editTaskLauncher.launch(intent)
            }
        }
        adapter.onDeleteBtnClickListener = object :OnBtnDeleteClickListener {
            override fun onBtnDeleteClick(task: Task, position: Int) {
                dao.deleteTask(task)
                adapter.deleteTask(position, task)
            }
        }
        adapter.onBtnDoneClickListener = object :OnBtnDoneClickListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onBtnDoneClick(task: Task, position: Int) {
                task.isDone = true
                dao.updateTask(task)
                adapter.updateTask(task,position)
            }
        }

    }
    override fun onStart() {
        super.onStart()
        loadAllTasksOfDate(getSelectedDate().timeInMillis)


    }

    private fun loadAllTasksOfDate(date: Long) {
       val tasks = dao.getAllTasksByDate(date).toMutableList()
        adapter.setTasksList(tasks)
    }

    private fun getSelectedDate():Calendar{
        val calendar = Calendar.getInstance()
       if (binding.calendarView.selectedDate != null){
           calendar.set(Calendar.YEAR, binding.calendarView.selectedDate!!.year)
        }
        binding.calendarView.selectedDate?.let { date->
            calendar.set(Calendar.YEAR, date.year)
            calendar.set(Calendar.MONTH, date.month-1)
            calendar.set(Calendar.DAY_OF_MONTH, date.day)
        }
        calendar.clearTime()
        return calendar
    }
}