package com.quiz.app.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.quiz.app.R
import com.quiz.app.databinding.FragmentQuestionBinding
import com.quiz.app.adapter.AnswerOptionsAdapter
import com.quiz.app.databinding.CustomDialogCorrectAnswerBinding
import com.quiz.app.databinding.CustomDialogLoadingAnimationBinding
import com.quiz.app.model.response.OptionData
import com.quiz.app.model.response.QuestionResponse
import com.quiz.app.utils.ConnectivityCheck
import com.quiz.app.utils.CustomItemClickListener
import com.quiz.app.utils.SharedPrefs
import com.quiz.app.viewmodel.QuestionViewModel
import es.dmoral.toasty.Toasty

class QuestionFragment : Fragment(), CustomItemClickListener {

    private var previousHighestScore = 0
    private var currentProgress = 0 ; private var optionClicked = false
    private var correctOption = "" ; private var correctOptionPosition = 0
    private var currentScore = 0 ; private var totalScore = 0
    private lateinit var loadingDialog: Dialog
    private lateinit var sharedPrefs: SharedPrefs
    private lateinit var binding: FragmentQuestionBinding
    private lateinit var questionViewModel: QuestionViewModel
    private lateinit var questionData: QuestionResponse
    private lateinit var optionsList: MutableList<OptionData>
    private lateinit var recyclerView: RecyclerView
    private var answerOptionsAdapter: AnswerOptionsAdapter? = null
    private var recyclerViewState: Parcelable? = null
    private var connectivityCheck: ConnectivityCheck = ConnectivityCheck()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentQuestionBinding.inflate(layoutInflater)
        questionViewModel = ViewModelProvider(this)[QuestionViewModel::class.java]

        sharedPrefs = SharedPrefs()
        sharedPrefs.init(requireActivity())

        try{
            previousHighestScore = Integer.parseInt(sharedPrefs.read("high_score_key", "").toString())
        } catch (e: Exception){
            e.printStackTrace()
        }

        loadingDialog = Dialog(requireActivity())
        recyclerView = binding.questionList

        optionsList = arrayListOf()
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL ,false)
        recyclerView.setHasFixedSize(false)
        answerOptionsAdapter = AnswerOptionsAdapter(requireActivity(), optionsList)
        answerOptionsAdapter!!.customClickListener(this)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                recyclerViewState = recyclerView.layoutManager!!.onSaveInstanceState()!!
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback (
            viewLifecycleOwner, object: OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    val alertDialogBuilder = AlertDialog.Builder(requireActivity())
                    alertDialogBuilder.apply {
                        setTitle("Alert !!!")
                        setMessage("Are you sure to quit?")

                        setPositiveButton("YES") { dialog, _ ->
                            if(previousHighestScore<totalScore){
                                sharedPrefs.write("high_score_key", totalScore.toString())
                            }

                            dialog.dismiss()

                            val startMain = Intent(Intent.ACTION_MAIN)
                            startMain.addCategory(Intent.CATEGORY_HOME)
                            startMain.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            requireActivity().startActivity(startMain)
                            requireActivity().finish()
                        }

                        setNegativeButton("NO") { dialog, _ ->
                            dialog.dismiss()
                        }
                    }

                    val alertDialog: AlertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
            })

        getQuestions()

        return binding.root
    }

    private fun getQuestions(){
        showLoadingDialog()

        if (connectivityCheck.checkInternet(requireActivity())) {
            questionViewModel.getQuestionsMethod()

            questionViewModel.quesResponse.observe(viewLifecycleOwner) {
                questionData = it

                Log.i("question_size", "${questionData.questions.size}")
                Log.i("question_data", "$questionData")

                loadingDialog.dismiss()
                manageQuestions(0)
            }

            questionViewModel.errorMessage.observe(viewLifecycleOwner) {
                Log.i("error_message", it)
                loadingDialog.dismiss()
            }

        } else {
            Toasty.error(requireActivity(), "No Internet", Toasty.LENGTH_LONG).show()
            loadingDialog.dismiss()
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun manageQuestions(position: Int) {
        optionClicked = false
        sharedPrefs.write("click_temp", "0")
        optionsList = arrayListOf()

        Glide.with(requireActivity()).load(questionData.questions[position].questionImageUrl).into(binding.questionImage)

        currentScore = questionData.questions[position].score
        correctOption = questionData.questions[position].correctAnswer

        binding.questionPoint.text = "$currentScore Point"
        binding.questionText.text = questionData.questions[position].question
        binding.questionNumber.text = "${position+1}/${questionData.questions.size}"

        val fields = questionData.questions[position].answers::class.java.declaredFields

        try {
            optionsList.add(OptionData(fields[0].name, questionData.questions[position].answers.A))
            optionsList.add(OptionData(fields[1].name, questionData.questions[position].answers.B))
            optionsList.add(OptionData(fields[2].name, questionData.questions[position].answers.C))
            optionsList.add(OptionData(fields[3].name, questionData.questions[position].answers.D))

            answerOptionsAdapter!!.setData(optionsList)
            recyclerView.adapter = answerOptionsAdapter
            answerOptionsAdapter!!.notifyDataSetChanged()
            recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)

        } catch (e: Exception){
            answerOptionsAdapter!!.setData(optionsList)
            recyclerView.adapter = answerOptionsAdapter
            answerOptionsAdapter!!.notifyDataSetChanged()
            recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)
        }

        for(i in 0 until optionsList.size){
            if(correctOption==optionsList[i].option){
                correctOptionPosition = i
            }
        }

        manageTimer(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun manageTimer(position: Int) {
        Handler(Looper.getMainLooper()).postDelayed({
            if(position<questionData.questions.size-1){
                if(!optionClicked){
                    correctAnswerDialog(correctOption)
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    currentProgress += 10
                    binding.answerProgress.progress = currentProgress
                    manageQuestions(position+1)

                }, 3000)

            } else if(position==questionData.questions.size-1) {
                currentProgress += 10
                binding.answerProgress.progress = currentProgress

                Handler(Looper.getMainLooper()).postDelayed({
                    if(previousHighestScore<totalScore){
                        sharedPrefs.write("high_score_key", totalScore.toString())
                    }

                    findNavController().navigate(R.id.action_questionFragment_to_welcomeFragment)
                }, 3000)
            }

        }, 10000)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onItemClick(position: Int, option: String, result: String, selected: TextView, correct: TextView) {
        optionClicked = true

        if(option==correctOption){
            totalScore += currentScore
            binding.scoreResult.text = totalScore.toString()

            selected.background = ContextCompat.getDrawable(requireActivity(), R.drawable.green_curved_border)

        } else {
            selected.background = ContextCompat.getDrawable(requireActivity(), R.drawable.red_curved_border)
            correctAnswerDialog(correctOption)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun correctAnswerDialog(correctOption: String) {
        val dialogBinding = CustomDialogCorrectAnswerBinding.inflate(layoutInflater)
        val dialog = Dialog(requireActivity())
        dialog.setContentView(dialogBinding.root)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog.window!!.setLayout(width, height)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        dialog.setCancelable(true)

        dialogBinding.correctAnsAlert.text = correctOption

        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
        }, 3000)

        dialog.show()
    }

    private fun showLoadingDialog(){
        val dialogBinding = CustomDialogLoadingAnimationBinding.inflate(layoutInflater)
        loadingDialog.setContentView(dialogBinding.root)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        loadingDialog.window!!.setLayout(width, height)
        loadingDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        loadingDialog.setCancelable(false)
        loadingDialog.show()
    }
}
