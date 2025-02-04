package com.route.todoc41.ui.home.fragments.tasks_fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.islamiapp.adapters.callbacks.OnTaskClickListener
import com.route.todoc41.R
import com.route.todoc41.R.color.green
import com.route.todoc41.database.MyDatabase
import com.route.todoc41.database.dao.TasksDao
import com.route.todoc41.database.entity.Task
import com.route.todoc41.databinding.ItemTaskBinding
import com.route.todoc41.ui.home.fragments.callbacks.OnBtnDeleteClickListener
import com.route.todoc41.ui.home.fragments.callbacks.OnBtnDoneClickListener
import com.route.todoc41.ui.util.getFormattedTime
import com.zerobranch.layout.SwipeLayout
import com.zerobranch.layout.SwipeLayout.SwipeActionsListener
import java.util.Calendar

class TasksAdapter:RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {
    private var tasksList = mutableListOf<Task>()
    var onTaskClickListener: OnTaskClickListener? = null
    var onBtnDoneClickListener:OnBtnDoneClickListener? = null
    var onDeleteBtnClickListener:OnBtnDeleteClickListener?=null


    @SuppressLint("NotifyDataSetChanged")
    fun setTasksList(tasks:MutableList<Task>){
        tasksList = tasks
        notifyDataSetChanged()
    }
    fun deleteTask(position: Int,task: Task){
        tasksList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,tasksList.size-position)
    }
    fun updateTask(task: Task,position: Int){
        tasksList[position]= task
        notifyItemChanged(position)
    }


   inner class TaskViewHolder(val binding: ItemTaskBinding):RecyclerView.ViewHolder(binding.root) {
       @SuppressLint("ResourceAsColor")
       fun bind(task: Task, position: Int) {
           binding.title.text = task.title
           val calendar = Calendar.getInstance()
           calendar.timeInMillis = task.time
           val hr = calendar.get(Calendar.HOUR)
           val minutes = calendar.get(Calendar.MINUTE)
           binding.time.text = getFormattedTime(hr, minutes)
       }

       fun taskIsDone(isDone: Boolean) {
           if (isDone) {
               binding.btnTaskIsDone.setBackgroundResource(R.drawable.done)
               binding.draggingBar.setImageResource(R.drawable.dragging_bar_done)
               binding.title.setTextColor(Color.GREEN)
           } else {
               binding.btnTaskIsDone.setBackgroundResource(R.drawable.check_mark)
               binding.draggingBar.setImageResource(R.drawable.dragging_bar)
               val blue = ContextCompat.getColor(itemView.context, R.color.blue)
               binding.title.setTextColor(blue)
           }

       }

   }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false))


    override fun getItemCount(): Int = tasksList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasksList[position]
        holder.bind(task,position)
        holder.binding.root.setOnClickListener {
            onTaskClickListener?.onTaskClick(task, position)
        }
        holder.taskIsDone(task.isDone)
        holder.binding.swipeLayout.close()
        holder.binding.swipeLayout.setOnActionsListener(object :SwipeActionsListener{
                override fun onOpen(direction: Int, isContinuous: Boolean) {
                    holder.binding.leftView.isClickable = true
                    if (direction == SwipeLayout.RIGHT) {
                        holder.binding.leftView.setOnClickListener {
                            onDeleteBtnClickListener?.onBtnDeleteClick(task,holder.adapterPosition)
            }
                    }
                }

                override fun onClose() {
                    holder.binding.leftView.isClickable = false
                }

            })

        holder.binding.btnTaskIsDone.setOnClickListener {
                onBtnDoneClickListener?.onBtnDoneClick(task,position)
            }


    }

}