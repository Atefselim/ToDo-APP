package com.route.todoc41.ui.home.fragments.callbacks

import com.route.todoc41.database.entity.Task

interface OnBtnDeleteClickListener {
    fun onBtnDeleteClick(task: Task, position: Int)
}