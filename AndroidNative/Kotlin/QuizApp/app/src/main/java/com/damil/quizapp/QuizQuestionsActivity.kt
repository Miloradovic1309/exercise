package com.damil.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {
    // Variable für Nutzernamen
    private var mUserName: String? = null
    private var mCurrentPosition = 1
    private var mQuestionList: ArrayList<Question>? = null
    private var mSelectedOption: Int = 0
    private var mCorrectAnswers: Int = 0

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        hideStatusBar()

        // Liest den extra String aus dem Intent aus und speichert diesen
        // in der Variable userName
        mUserName = intent.getStringExtra(Constans.USER_NAME)


        // Aufruf der Funktion, um neue Frage zu laden
        setQuestion()

        // OnClickListener für die vier Auswahloptionen
        tvOptionOne.setOnClickListener(this)
        tvOptionTwo.setOnClickListener(this)
        tvOptionThree.setOnClickListener(this)
        tvOptionFour.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)


    }

    /*
    Funktion um die aktuelle Frage zu laden
     */
    private fun setQuestion() {

        // Abrufen der Fragenliste -> unbedingt vor Abfrage in Zeile 53
        mQuestionList = Constans.getQuestions()

        // Zurücksetzen des Layouts der Fragenoptionen
        defaultOptionsView()

        // Wenn letzte Frage, dann bekommt Button den Text beenden
        if (mCurrentPosition == mQuestionList!!.size) {
            btnSubmit.text = "BEENDEN"
            // Sonst wird der Text auf Bestätigen zurückgesetzt
        } else {
            btnSubmit.text = "BESTÄTIGEN"
        }


        // Aktuelle Frage aus Fragenliste in Objekt der Datenklasse Question
        val question = mQuestionList!![mCurrentPosition - 1]

        // Progressbar Fortschritt zuweisen
        progressBar.progress = mCurrentPosition
        // Textfeld des Fortschrittes aktualisieren
        tvProgress.text = "$mCurrentPosition/" + progressBar.max

        // Daten aus aktueller Frage den UI Elementen zuweisen
        tvQuestion.text = question!!.question
        ivFlag.setImageResource(question.image)
        tvOptionOne.text = question.optionOne
        tvOptionTwo.text = question.optionTwo
        tvOptionThree.text = question.optionThree
        tvOptionFour.text = question.optionFour
    }


    /*
    Zurücksetzen der Auswahloptionen auf Standardlayout
     */
    private fun defaultOptionsView() {
        // Vier Textfelder für Antworten in ArrayList speichern
        val options = ArrayList<TextView>()
        options.add(0, tvOptionOne)
        options.add(1, tvOptionTwo)
        options.add(2, tvOptionThree)
        options.add(3, tvOptionFour)

        // ArrayList durchlaufen und jedes TextView anpassen
        for (option in options) {
            // Textfarbe auf Anfangswert
            option.setTextColor(Color.parseColor("#7A8089"))
            // Typeface auf Default (und nicht mehr Bold)
            option.typeface = Typeface.DEFAULT
            // Hintergrund auf default_option_border setzen
            option.background = ContextCompat.getDrawable(
                    this, R.drawable.default_option_border
            )
        }
    }

    /*
    onClick Methode um abzufragen, welches TextView ausgewählt wurde und darauf zu reagieren
     */
    override fun onClick(v: View?) {
        // When-Abfrage mit der id des, als Parameter, übergebenen Views v
        // id ist die festgelegte in der Layout-Datei activity_quiz_questions.xml
        when (v?.id) {
            // Option 1
            R.id.tvOptionOne -> {
                // Aufruf der Funktion mit Option 1 (Textview tvOptionOne)
                selectedOptionView(tvOptionOne, 1)
            }
            // Option 2
            R.id.tvOptionTwo -> {
                // Aufruf der Funktion mit Option 2 (Textview tvOptionTwo)
                selectedOptionView(tvOptionTwo, 2)
            }
            // Option 3
            R.id.tvOptionThree -> {
                // Aufruf der Funktion mit Option 3 (Textview tvOptionThree)
                selectedOptionView(tvOptionThree, 3)
            }
            // Option 4
            R.id.tvOptionFour -> {
                // Aufruf der Funktion mit Option 4 (Textview tvOptionFour)
                selectedOptionView(tvOptionFour, 4)
            }
            R.id.btnSubmit -> {
                /*
                Wenn mSelectedOption 0 ist, soll die nächste Frage geladen werden
                 */
                if (mSelectedOption == 0) {
                    // Zahl der aktuellen Frage um 1 erhöhen
                    mCurrentPosition++

                    when {
                        // Wenn aktuelle Position innerhalb der Größe der Fragenliste ist
                        mCurrentPosition <= mQuestionList!!.size -> {
                            // Funktion lädt neue Frage
                            setQuestion()
                        }
                        else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constans.USER_NAME, mUserName)
                            intent.putExtra(Constans.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constans.TOTAL_QUESTIONS, mQuestionList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                    // Wenn ausgewählte Option nicht 0 ist, soll Nutzerauswahl visualisitert werden
                } else {
                    // Speichere die aktuelle Frage in question
                    val question = mQuestionList?.get(mCurrentPosition - 1)
                    // Wenn korrekte Antwort ungleich der gegebenen ist
                    if (question!!.correctAnswer != mSelectedOption) {
                        // Setze Hintergrund der gegebenen Antwort auf falsch (wrong_option_border.xml)
                        answerView(mSelectedOption, R.drawable.wrong_option_border)
                    }else{
                        mCorrectAnswers++
                    }
                    // Richtige Option muss in jedem Fall gesetzt werden, unabhängig von Nutzerauswahl
                    answerView(question!!.correctAnswer, R.drawable.correct_option_border)

                    // Wenn die aktuelle Position gleich der Größe der Fragenliste ist
                    if (mCurrentPosition == mQuestionList!!.size) {
                        // Der Buttontext wird auf beenden gesetzt, weil es keine weitere Frage gibt
                        btnSubmit.text = "BEENDEN"
                    } else {
                        // Sonst soll der Text nächste Frage anzeigen, weil es weitere Fragen gibt
                        btnSubmit.text = "NÄCHSTE FRAGE"
                    }
                    // Ausgewählte Option soll auf 0 gesetzt werden -> Dadurch wird bei erneutem Klick
                    // auf btnSubmit die Abfrage in Zeile 133 true ergeben
                    mSelectedOption = 0
                }
            }
        }
    }

    /*
    Funktion bekommt gewählte Antwort und eine id des Bildes übergeben.
    Durch eine When-Abfrage wird die übergebene Antwort geprüft und der
    Hintergrund des dazugehörigen Textfeldes wird überschrieben.
     */
    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            // Option 1
            1 -> {
                // Hintergrund des Textfeldes zu der Option wird mit übergebener Bild-id Überschrieben
                tvOptionOne.background = ContextCompat.getDrawable(
                        this, drawableView
                )
            }
            // Option 2
            2 -> {
                // Hintergrund des Textfeldes zu der Option wird mit übergebener Bild-id Überschrieben
                tvOptionTwo.background = ContextCompat.getDrawable(
                        this, drawableView
                )
            }
            // Option 3
            3 -> {
                // Hintergrund des Textfeldes zu der Option wird mit übergebener Bild-id Überschrieben
                tvOptionThree.background = ContextCompat.getDrawable(
                        this, drawableView
                )
            }
            // Option 4
            4 -> {
                // Hintergrund des Textfeldes zu der Option wird mit übergebener Bild-id Überschrieben
                tvOptionFour.background = ContextCompat.getDrawable(
                        this, drawableView
                )
            }
        }
    }

    /*
    Funktion um ausgewähltes Textfeld im Aussehen zu ändern
    -> Rand wird lila, Text wird dunkel, Text wird Bold
     */
    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
        // Zurücksetzen aller Textfelder auf Anfangsaussehen
        defaultOptionsView()
        // Globale Variable der ausgewählten Option setzen
        mSelectedOption = selectedOptionNum

        // Textfarbe des übergebenen Textviews tv ändern
        tv.setTextColor(Color.parseColor("#363A43"))
        // Typeface des übergebenen Textviews tv auf fettgedruckt ändern
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        // Hintergrund des übergebenen Textviews tv auf selected_option_border.xml ändern
        tv.background = ContextCompat.getDrawable(
                this, R.drawable.selected_option_border
        )
    }


}