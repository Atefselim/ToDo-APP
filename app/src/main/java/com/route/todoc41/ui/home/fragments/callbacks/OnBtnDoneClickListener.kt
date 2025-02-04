package com.route.todoc41.ui.home.fragments.callbacks

import com.route.todoc41.database.entity.Task

interface OnBtnDoneClickListener {
    fun onBtnDoneClick(task: Task, position: Int)
}