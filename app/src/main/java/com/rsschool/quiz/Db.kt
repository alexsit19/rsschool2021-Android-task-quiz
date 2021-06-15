package com.rsschool.quiz

class Db {
    companion object {
        val questionsList: Map<Int, String> = mapOf(1 to "Question 1", 2 to "Question 2", 3 to "Question 3", 4 to "Question 4", 5 to "Question 5")
        val rightAnswersList: Map<Int, String> = mapOf(1 to "3", 2 to "4", 3 to "5", 4 to "1", 5 to "2")
        val answersList: Map<Int, List<String>> = mapOf(
            1 to listOf("Anw 1", "Anw 2", "Anw 3", "Anw 4", "Anw 5"),
            2 to listOf("Anw 1", "Anw 2", "Anw 3", "Anw 4", "Anw 5"),
            3 to listOf("Anw 1", "Anw 2", "Anw 3", "Anw 4", "Anw 5"),
            4 to listOf("Anw 1", "Anw 2", "Anw 3", "Anw 4", "Anw 5"),
            5 to listOf("Anw 1", "Anw 2", "Anw 3", "Anw 4", "Anw 5"))
    }
}