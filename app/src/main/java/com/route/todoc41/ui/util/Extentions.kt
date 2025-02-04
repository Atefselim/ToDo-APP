package com.route.todoc41.ui.util

import java.util.Calendar

fun Calendar.clearTime(){
    set(Calendar.HOUR,0)
    set(Calendar.MINUTE,0)
    set(Calendar.SECOND,0)
    set(Calendar.MILLISECOND,0)
}

fun Calendar.clearDate(){
    set(Calendar.YEAR,0)
    set(Calendar.MONTH,0)
    set(Calendar.DAY_OF_MONTH,0)
}

fun Calendar.clearSeconds(){
    set(Calendar.SECOND,0)
    set(Calendar.MILLISECOND,0)
}