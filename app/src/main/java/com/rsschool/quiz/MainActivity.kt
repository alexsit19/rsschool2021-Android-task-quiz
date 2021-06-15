package com.rsschool.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.rsschool.quiz.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), CallBackInterface {

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

    override fun replaceFragment(questionCount: Int?, currentAnswers: MutableMap<Int?, String>) {
        supportFragmentManager.beginTransaction()
            .replace(binding.hostFragment.id, QuizFragment.newInstance(questionCount, currentAnswers))
            .commit()
    }
}