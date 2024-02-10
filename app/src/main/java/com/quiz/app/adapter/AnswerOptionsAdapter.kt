package com.quiz.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quiz.app.R
import com.quiz.app.model.response.OptionData
import com.quiz.app.utils.CustomItemClickListener
import com.quiz.app.utils.SharedPrefs

class AnswerOptionsAdapter(
    private var context: Context,
    private var optionsList: MutableList<OptionData>,

): RecyclerView.Adapter<AnswerOptionsAdapter.MyViewHolder>() {

    private var temp = 0
    private val sharedPrefs = SharedPrefs()
    private var customItemClickListener: CustomItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.answer_option_adapter, parent, false)
        return MyViewHolder(view)
    }

    fun setData(optionsList: MutableList<OptionData>) {
        this.optionsList = optionsList
    }

    fun customClickListener(customItemClickListener: CustomItemClickListener){
        this.customItemClickListener = customItemClickListener
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        sharedPrefs.init(context)
        temp = Integer.parseInt(sharedPrefs.read("click_temp", "").toString())
        val dataInfo: OptionData = optionsList[position]

        val option = dataInfo.option
        val result = dataInfo.result

        holder.optionText.text = result

        holder.optionText.setOnClickListener {
            if(temp==0){
                customItemClickListener!!.onItemClick(position, option, result, holder.optionText, holder.optionText)
                temp++
            }
        }
    }

    override fun getItemCount(): Int {
        return optionsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var optionText: TextView = itemView.findViewById(R.id.optionText)
    }
}
