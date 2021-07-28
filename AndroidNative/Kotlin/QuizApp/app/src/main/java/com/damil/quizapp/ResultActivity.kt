package com.damil.quizapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        hideStatusBar()

        val username = intent.getStringExtra(Constans.USER_NAME)
        val correctAnswers = intent.getIntExtra(Constans.CORRECT_ANSWERS, 0)
        val totalQuestion = intent.getIntExtra(Constans.TOTAL_QUESTIONS, 10)

        tvName.text = username
        tvScore.text = "Du hast $correctAnswers von $totalQuestion richtig beantwortet"

        btnFinish.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }


    fun hideStatusBar() {
        if (Build.VERSION.SDK_INT < 30 ) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        } else {
            window.setDecorFitsSystemWindows(false)
            val controller = window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}