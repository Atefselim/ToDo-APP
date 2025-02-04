package com.route.todoc41.ui.util

import kotlin.math.min

fun getFormattedTime(hour:Int,minutes:Int):String{
    val minutesString = if (minutes==0) "00" else minutes.toString()
    return "${getHourIn12(hour)}:$minutesString ${getAmPm(hour)}"
}

fun getHourIn12(hour: Int):Int{
    return if (hour>12) hour - 12 else if (hour==0) 12 else hour
}

fun getAmPm(hour: Int):String{
    return if (hour<12) "AM" else "PM"
}