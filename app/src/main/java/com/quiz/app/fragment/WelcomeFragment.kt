package com.quiz.app.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.quiz.app.R
import com.quiz.app.databinding.FragmentWelcomeBinding
import com.quiz.app.utils.SharedPrefs
import java.lang.Exception

class WelcomeFragment : Fragment() {

    private lateinit var sharedPrefs: SharedPrefs
    private lateinit var binding: FragmentWelcomeBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWelcomeBinding.inflate(layoutInflater)

        sharedPrefs = SharedPrefs()
        sharedPrefs.init(requireActivity())

        try{
            val highScore = sharedPrefs.read("high_score_key", "").toString()

            if(highScore.isNotEmpty()){
                binding.highScore.text = "$highScore Points"

            } else {
                binding.highScore.text = "0 Points"
            }

        } catch (e: Exception){
            binding.highScore.text = "0 Point"
        }

        binding.startQuiz.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_questionFragment)
        }

        return binding.root
    }
}
