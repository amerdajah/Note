package com.amerdev.note.util

import java.text.SimpleDateFormat
import java.util.*

object Utility {


    private const val TAG = "Utility"// Find todays date

    //MUST USE LOWERCASE 'y'. API 23- can't use uppercase
    val currentTimeStamp: String?
        get() = try {
            val dateFormat =
                SimpleDateFormat("MM-yyyy") //MUST USE LOWERCASE 'y'. API 23- can't use uppercase
            dateFormat.format(Date())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    fun getMonthFromNumber(monthNumber: String?): String {
        return when (monthNumber) {
            "01" -> {
                "Jan"
            }
            "02" -> {
                "Feb"
            }
            "03" -> {
                "Mar"
            }
            "04" -> {
                "Apr"
            }
            "05" -> {
                "May"
            }
            "06" -> {
                "Jun"
            }
            "07" -> {
                "Jul"
            }
            "08" -> {
                "Aug"
            }
            "09" -> {
                "Sep"
            }
            "10" -> {
                "Oct"
            }
            "11" -> {
                "Nov"
            }
            "12" -> {
                "Dec"
            }
            else -> {
                "Error"
            }
        }
    }
}