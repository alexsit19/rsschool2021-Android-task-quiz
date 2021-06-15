package com.rsschool.quiz

interface CallBackInterface {

    fun replaceFragment(questionCount: Int?, currentAnswers: MutableMap<Int?, String>)
}