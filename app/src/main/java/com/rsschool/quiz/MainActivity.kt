package com.rsschool.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.rsschool.quiz.databinding.ActivityMainBinding
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), QuizFragmentInterface, ResultFragmentInterface {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("DEBUG", "Activity.onCreate")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.hostFragment.id, QuizFragment())
                .commit()
        }
    }

    override fun replaceFragment(reset: Boolean, questionCount: Int?, currentAnswers: MutableMap<Int?, String>?) {
        supportFragmentManager.beginTransaction()
            .replace(binding.hostFragment.id, QuizFragment.newInstance(reset, questionCount, currentAnswers))
            .commit()
    }

    override fun openResultFragment(result: Int) {
        supportFragmentManager.beginTransaction()
            .replace(binding.hostFragment.id, ResultFragment.newInstance(result))
            .commit()
        Log.d("DEBUG", "openResultFragmentFromActivity $result")
    }

    override fun close() {
        finishAffinity()
        exitProcess(0)
    }

    override fun reset(reset: Boolean) {
        replaceFragment(true)
    }

    override fun share() {
        TODO("Not yet implemented")
    }
}