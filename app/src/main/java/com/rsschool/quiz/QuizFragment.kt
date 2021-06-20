package com.rsschool.quiz

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.rsschool.quiz.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = requireNotNull(_binding)
    private var questionCount: Int? = 1
    private var clickListener: QuizFragmentInterface? = null
    private var currentAnswers = mutableMapOf<Int?, String>(1 to "0", 2 to "0", 3 to "0", 4 to "0", 5 to "0")
    private var reset = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var map: Map<Int?, String>?

        var arg: Bundle? = null
        try {
            arg = requireArguments()
            reset = arg.getBoolean(IS_RESET)

        } catch (e: IllegalStateException) {

        }

        if(arg != null && !reset) {
            map = mutableMapOf()
            for (i in 1..5) {
                val value = arg.getString(i.toString())
                map[i] = value ?: "0"
            }

            currentAnswers = map
            questionCount = arg.getInt(QUESTION_COUNT)

        } else {
            reset = false

        }

        val themId = getThemeId()
        val typedValue = TypedValue()
        inflater.context.setTheme(themId)
        val currentTheme = context?.theme
        currentTheme?.resolveAttribute(android.R.attr.statusBarColor, typedValue, true)
        val window = activity?.window


        val mode = context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        when (mode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                window?.statusBarColor = Color.BLACK

            }
            Configuration.UI_MODE_NIGHT_NO -> {
                window?.statusBarColor = typedValue.data

            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                window?.statusBarColor = typedValue.data
            }
        }


        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun changeTheme(): Unit {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        clickListener = context as MainActivity
    }

    override fun onDetach() {
        super.onDetach()
        clickListener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiUpdate()

        binding.toolbar.getChildAt(1)?.setOnClickListener {
            getPreviousQuestion()
            clickListener?.replaceFragment(reset, questionCount, currentAnswers)
            uiUpdate()
        }

        binding.previousButton.setOnClickListener {
            getPreviousQuestion()
            clickListener?.replaceFragment(reset, questionCount, currentAnswers)
            uiUpdate()
        }

        binding.nextButton.setOnClickListener {
            getNextQuestion()
            if (questionCount == 6) {
                val score = countRightAnswers()
                clickListener?.openResultFragment(score, currentAnswers)

            } else {
                binding.toolbar.title = "Question $questionCount"
                clickListener?.replaceFragment(reset, questionCount, currentAnswers)
                uiUpdate()

            }
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkId ->
            when (checkId) {
                binding.optionOne.id -> currentAnswers[questionCount] = "1"
                binding.optionTwo.id -> currentAnswers[questionCount] = "2"
                binding.optionThree.id -> currentAnswers[questionCount] = "3"
                binding.optionFour.id -> currentAnswers[questionCount] = "4"
                binding.optionFive.id -> currentAnswers[questionCount] = "5"

            }
            binding.nextButton.isEnabled = true
        }
    }

    fun countRightAnswers(): Int {
        var score = 0
        for (i in 1..5) {
            if (currentAnswers[i] == Db.rightAnswersList[i]) {
                score++
            }
        }
        return score * 20
    }

    fun getThemeId(): Int {

        when (questionCount) {
            1 -> {
                return R.style.Theme_Quiz_First

            }
            2 -> {
                return R.style.Theme_Quiz_Second

            }
            3 -> {
                return R.style.Theme_Quiz_Third

            }
            4 -> {
                return R.style.Theme_Quiz_Fourth

            }
            5 -> {
                return R.style.Theme_Quiz_Fifth

            }
            else -> {
                return R.style.Theme_Quiz

            }
        }
    }

    private fun uiUpdate() {
        binding.apply {
            toolbar.title = "Question $questionCount"
            question.text = Db.questionsList[questionCount]
            optionOne.text = Db.answersList[questionCount]?.get(0)
            optionTwo.text = Db.answersList[questionCount]?.get(1)
            optionThree.text = Db.answersList[questionCount]?.get(2)
            optionFour.text = Db.answersList[questionCount]?.get(3)
            optionFive.text = Db.answersList[questionCount]?.get(4)

            if (currentAnswers[questionCount] == "0") {
                binding.radioGroup.clearCheck()
                nextButton.isEnabled = false

            } else {
                when (currentAnswers[questionCount]) {
                    "1" -> {
                        optionOne.isChecked = true
                        currentAnswers[questionCount] = "1"
                    }
                    "2" -> {
                        optionTwo.isChecked = true
                        currentAnswers[questionCount] = "2"
                    }
                    "3" -> {
                        optionThree.isChecked = true
                        currentAnswers[questionCount] = "3"
                    }
                    "4" -> {
                        optionFour.isChecked = true
                        currentAnswers[questionCount] = "4"
                    }
                    "5" -> {
                        optionFive.isChecked = true
                        currentAnswers[questionCount] = "5"
                    }
                }
            }

            when(questionCount) {
                in 2..5 -> { previousButton.isEnabled = true
                            if (questionCount == 5) {
                                nextButton.text = "submit"

                            } else {
                                nextButton.text = "next"

                            }
                }
                1 -> { previousButton.isEnabled = false
                       binding.toolbar.navigationIcon = null }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getNextQuestion() {
        questionCount = questionCount?.plus(1)

    }

    private fun getPreviousQuestion() {
        questionCount = questionCount?.minus(1)
    }

    companion object {

        private const val QUESTION_COUNT = "COUNT"
        private const val CURRENT_ANSWERS = "CURRENT_ANSWERS"
        private const val IS_RESET = "reset"

        @JvmStatic
        fun newInstance(reset: Boolean, questionCount: Int? = null, currentAnswers: MutableMap<Int?, String>? = null): QuizFragment {
            val fragment = QuizFragment()
            val args = Bundle()
            //использовать bundleOf не представляется возможным, т.к. нужно в bundle поместить map с моими типами параметров
            args.putBoolean(IS_RESET, reset)
            questionCount?.let { args.putInt(QUESTION_COUNT, it) }
            if (currentAnswers != null) {
                for (item in currentAnswers) {
                    args.putString(item.key.toString(), item.value)

                }
            }
            fragment.arguments = args
            return fragment
        }
    }
}

