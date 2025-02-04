package com.example.islamiapp.adapters.callbacks

import com.route.todoc41.database.entity.Task

interface OnTaskClickListener {
    fun onTaskClick(task: Task , position: Int)
}