package com.route.todoc41.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.route.todoc41.database.entity.Task

@Dao
interface TasksDao {

    @Insert
    fun insertNewTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("select * from task")
    fun getAllTasks():List<Task>

    @Query("select * from task where date = :date")
    fun getAllTasksByDate(date:Long):List<Task>

    @Query("select * from task where id = :id")
    fun getTaskById(id:Int):Task?

    @Query("select * from task where isDone = 0")
    fun getUnCompletedTasks():List<Task>
}
