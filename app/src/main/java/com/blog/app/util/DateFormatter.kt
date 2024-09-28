package com.blog.app.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {

    fun formattedDateTime(inputDateTime: String): String{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy, hh:mm a", Locale.getDefault())

        val parsedDate: Date? = inputFormat.parse(inputDateTime)
        val formattedDate: String = outputFormat.format(parsedDate!!)

        return formattedDate
    }

}
