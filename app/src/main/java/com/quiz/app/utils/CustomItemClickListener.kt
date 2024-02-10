package com.quiz.app.utils

import android.widget.TextView

interface CustomItemClickListener {
    fun onItemClick(position: Int, option: String, result: String, selected: TextView, correct: TextView)
}
