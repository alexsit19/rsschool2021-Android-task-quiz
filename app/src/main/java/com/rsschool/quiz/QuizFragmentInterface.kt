package com.rsschool.quiz

interface QuizFragmentInterface {

    fun replaceFragment(reset: Boolean, questionCount: Int? = null, currentAnswers: MutableMap<Int?, String>? = null)
    fun openResultFragment(result: Int, answers: Map<Int?, String>)

}