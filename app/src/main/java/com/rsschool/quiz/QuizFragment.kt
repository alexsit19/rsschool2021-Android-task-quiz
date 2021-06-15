package com.rsschool.quiz

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rsschool.quiz.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var binding: FragmentQuizBinding? = null
    private var questionCount: Int? = 1
    private var clickListener: CallBackInterface? = null
    private var currentAnswers = mutableMapOf<Int?, String>(1 to "0", 2 to "0", 3 to "0", 4 to "0", 5 to "0")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var map: Map<Int?, String>? = null

        var arg: Bundle? = null
        try {
            arg = requireArguments()
        } catch (e: IllegalStateException) {

        }

        if(arg != null) {
            map = mutableMapOf<Int?, String>()
            for (i in 1..5) {
                val value = arg.getString(i.toString())
                map[i] = value ?: "0"
            }

            currentAnswers = map
            questionCount = arg.getInt(QUESTION_COUNT)
            val themId = getThemeId()
            val typedValue = TypedValue()
            inflater.context.setTheme(themId)
            val currentTheme = context?.theme
            currentTheme?.resolveAttribute(android.R.attr.statusBarColor, typedValue, true)
            //binding?.toolbar?.setBackgroundColor(typedValue.data)
            val window = activity?.window
            window?.statusBarColor = typedValue.data



        }

        binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding?.root
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
        binding?.previousButton?.setOnClickListener {
            getPreviousQuestion()
            clickListener?.replaceFragment(questionCount, currentAnswers)
            //themeChange()
            uiUpdate()
        }

        binding?.nextButton?.setOnClickListener {
            getNextQuestion()
            binding?.toolbar?.title = "Question $questionCount"
            //themeChange()
            clickListener?.replaceFragment(questionCount, currentAnswers)
            uiUpdate()

        }

        binding?.radioGroup?.setOnCheckedChangeListener { _, checkId ->
            when (checkId) {
                binding?.optionOne?.id -> currentAnswers[questionCount] = "1"
                binding?.optionTwo?.id -> currentAnswers[questionCount] = "2"
                binding?.optionThree?.id -> currentAnswers[questionCount] = "3"
                binding?.optionFour?.id -> currentAnswers[questionCount] = "4"
                binding?.optionFive?.id -> currentAnswers[questionCount] = "5"

            }
            binding?.nextButton?.isEnabled = true

        }
    }

    fun getThemeId(): Int {
        var typedValue = TypedValue()
        val theme = context?.theme
        //val style = context?.obtainStyledAttributes(style.Theme_Quiz_Second, )
        val theme1 = R.style.Theme_Quiz_First
        //val c = context?.g

        when (questionCount) {
            1 -> {
                Log.d("DEBUG", "HERE")
                return R.style.Theme_Quiz_First

            }
            2 -> {
                return R.style.Theme_Quiz_Second
//                theme?.resolveAttribute(android.R.attr.statusBarColor, typedValue, true)
//                binding?.toolbar?.setBackgroundColor(typedValue.data)
            }
            3 -> {
                Log.d("DEBUG", "HERE 3")
                return R.style.Theme_Quiz_First

            }
            4 -> {
                return R.style.Theme_Quiz_Second
            }
            5 -> {
                return R.style.Theme_Quiz_First
            }
            else -> {
                return R.style.Theme_Quiz
            }
        }
    }

    fun uiUpdate() {
        binding?.apply {
            toolbar.title = "Question $questionCount"
            question.text = Db.questionsList[questionCount]
            optionOne.text = Db.answersList[questionCount]?.get(0)
            optionTwo.text = Db.answersList[questionCount]?.get(1)
            optionThree.text = Db.answersList[questionCount]?.get(2)
            optionFour.text = Db.answersList[questionCount]?.get(3)
            optionFive.text = Db.answersList[questionCount]?.get(4)

            if (currentAnswers[questionCount] == "0") {
                binding?.radioGroup?.clearCheck()
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

            if (questionCount != 1) {
                previousButton.isEnabled = true

            } else {
                previousButton.isEnabled = false
            }
        }
        Log.d("DEBUG", "${currentAnswers.toString()}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun getNextQuestion() {
        questionCount = questionCount?.plus(1)
        Log.d("DEBUG", "NextQuestion $questionCount")

    }

    fun getPreviousQuestion() {
        questionCount = questionCount?.minus(1)
    }

    companion object {

        private const val QUESTION_COUNT = "COUNT"
        private const val CURRENT_ANSWERS = "CURRENT_ANSWERS"

        @JvmStatic
        fun newInstance(questionCount: Int?, currentAnswers: MutableMap<Int?, String>): QuizFragment {
            val fragment = QuizFragment()
            val args = Bundle()
            questionCount?.let { args.putInt(QUESTION_COUNT, it) }
            for (item in currentAnswers) {
                args.putString(item.key.toString(), item.value)
            }
            fragment.arguments = args
            return fragment
        }

    }
}

