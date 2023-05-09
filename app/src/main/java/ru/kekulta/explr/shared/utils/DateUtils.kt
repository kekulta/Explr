package ru.kekulta.explr.shared.utils

import android.content.res.Resources
import ru.kekulta.explr.R
import java.util.Calendar

fun Resources.getMonth(number: Int): String = when (number) {
    0 -> getString(R.string.month_january)
    1 -> getString(R.string.month_february)
    2 -> getString(R.string.month_march)
    3 -> getString(R.string.month_april)
    4 -> getString(R.string.month_may)
    5 -> getString(R.string.month_june)
    6 -> getString(R.string.month_july)
    7 -> getString(R.string.month_august)
    8 -> getString(R.string.month_september)
    9 -> getString(R.string.month_october)
    10 -> getString(R.string.month_november)
    11 -> getString(R.string.month_december)
    else -> throw IllegalArgumentException()
}

fun month(time: Long): Int {
    val cal = Calendar.getInstance()
    cal.timeInMillis = time
    return cal.get(Calendar.MONTH)
}

fun dayOfMonth(time: Long): Int {
    val cal = Calendar.getInstance()
    cal.timeInMillis = time
    return cal.get(Calendar.DAY_OF_MONTH)
}


