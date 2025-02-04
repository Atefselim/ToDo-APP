package com.route.todoc41.database.entity
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Parcelize
@Entity
data class Task (
    @PrimaryKey(autoGenerate = true)
    var id: Int? =null,
    var title: String,
    var description: String? = null,
    var date: Long,
    var time: Long,
    var isDone: Boolean = false
) : Parcelable