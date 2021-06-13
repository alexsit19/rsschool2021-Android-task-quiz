package com.rsschool.quiz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
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
        quizViewModel.questionCount.observe(viewLifecycleOwner, Observer { newQuestionCount ->
            questionCount = newQuestionCount

        })

        questionCount = quizViewModel.questionCount.value
        uiUpdate()

        binding?.previousButton?.setOnClickListener {
            getPreviousQuestion()
            uiUpdate()
        }

        binding?.nextButton?.setOnClickListener {
            getNextQuestion()
            binding?.toolbar?.title = "Question $questionCount"
            uiUpdate()
        }

        binding?.radioGroup?.setOnCheckedChangeListener { _, checkId ->
            when(checkId) {
                binding?.optionOne?.id -> quizViewModel.currentAnswers[questionCount] = "1"
                binding?.optionTwo?.id -> quizViewModel.currentAnswers[questionCount] = "2"
                binding?.optionThree?.id -> quizViewModel.currentAnswers[questionCount] = "3"
                binding?.optionFour?.id -> quizViewModel.currentAnswers[questionCount] = "4"
                binding?.optionFive?.id -> quizViewModel.currentAnswers[questionCount] = "5"

            }
            binding?.nextButton?.isEnabled = true
        }
    }

    fun uiUpdate() {
        binding?.apply {
            question.text = quizViewModel.questionsList[questionCount]
            optionOne.text = quizViewModel.answersList[questionCount]?.get(0)
            optionTwo.text = quizViewModel.answersList[questionCount]?.get(1)
            optionThree.text = quizViewModel.answersList[questionCount]?.get(2)
            optionFour.text = quizViewModel.answersList[questionCount]?.get(3)
            optionFive.text = quizViewModel.answersList[questionCount]?.get(4)

            if (quizViewModel.currentAnswers[questionCount] == "0") {
                binding?.radioGroup?.clearCheck()
                nextButton.isEnabled = false
                Log.d("DEBUG", "IF 0")

            } else {
                when (quizViewModel.currentAnswers[questionCount]) {
                    "1" -> {
                        optionOne.isChecked = true
                        quizViewModel.currentAnswers[questionCount] = "1"
                    }
                    "2" -> {
                        optionTwo.isChecked = true
                        quizViewModel.currentAnswers[questionCount] = "2"
                    }
                    "3" -> {
                        optionThree.isChecked = true
                        quizViewModel.currentAnswers[questionCount] = "3"
                    }
                    "4" -> {
                        optionFour.isChecked = true
                        quizViewModel.currentAnswers[questionCount] = "4"
                    }
                    "5" -> {
                        optionFive.isChecked = true
                        quizViewModel.currentAnswers[questionCount] = "5"
                    }
                }
            }

            if(quizViewModel.questionCount.value != 1) {
                previousButton.isEnabled = true

            } else {
                previousButton.isEnabled = false
            }
        }
        Log.d("DEBUG", "${quizViewModel.currentAnswers.toString()}")
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
