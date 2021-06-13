package com.rsschool.quiz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.rsschool.quiz.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var binding: FragmentQuizBinding? = null
    private lateinit var quizViewModel: QuizViewModel
    private var questionCount: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.previousButton?.isEnabled = false
        binding?.nextButton?.isEnabled = false
        quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        questionCount = quizViewModel.questionCount.value

        binding?.nextButton?.setOnClickListener {
            Log.d("DEBUG", "next button is pressed")
        }

        if (questionCount != 1) {
            binding?.previousButton?.isEnabled = true

        }

        binding?.previousButton?.setOnClickListener {
            getPreviousQuestion()
        }

        binding?.nextButton?.setOnClickListener {
            getNextQuestion()
            binding?.toolbar?.title = "Question $questionCount"
        }

        binding?.radioGroup?.setOnCheckedChangeListener{ _, checkId ->
            when(checkId) {

                binding?.optionOne?.id -> Log.d("DEBUG", "опция 1 выбрана")
                binding?.optionTwo?.id -> Log.d("DEBUG", "опция 2 выбрана")
                binding?.optionThree?.id -> Log.d("DEBUG", "опция 3 выбрана")
                binding?.optionFour?.id -> Log.d("DEBUG", "опция 4 выбрана")
                binding?.optionFive?.id -> Log.d("DEBUG", "опция 5 выбрана")

            }
            binding?.nextButton?.isEnabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun getNextQuestion() {
        quizViewModel.incrementQuestionCount()
    }

    fun getPreviousQuestion() {
        quizViewModel.decrementQuestionCount()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuizFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuizFragment().apply {

                }
            }
    }
