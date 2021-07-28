package com.damil.quizapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        hideStatusBar();

        btnStart.setOnClickListener {
            if(etName.text.toString().isEmpty()){
                Toast.makeText(this, "Bitte gib deinen Namen an", Toast.LENGTH_LONG).show();
            }
            else{
                val intent = Intent(this, QuizQuestionsActivity::class.java);
                intent.putExtra(Constans.USER_NAME, etName.text.toString())
                startActivity(intent);
                finish()
            }
        }






    }


    fun hideStatusBar(){
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