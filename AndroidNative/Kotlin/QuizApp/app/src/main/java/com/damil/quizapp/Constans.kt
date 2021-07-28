package com.damil.quizapp

object Constans{
    const val USER_NAME: String="user_name"
    const val CORRECT_ANSWERS: String = "correct_answers"
    const val TOTAL_QUESTIONS: String = "total_questions"

    fun getQuestions(): ArrayList<Question>{
        val questionList = ArrayList<Question>()

        // 1
        val question1 = Question(1, "Zu welchem Land gehört diese Flagge?",
                R.drawable.ic_flag_of_argentina, "Argentien", "Armenien",
                "Australien", "Honduras", 1)
        questionList.add(question1)
        // 2
        val question2 = Question(
                2, "Zu welchem Land gehört diese Flagge?",
                R.drawable.ic_flag_of_australia,
                "Angola", "Österreich",
                "Australien", "Armenien", 3
        )

        questionList.add(question2)

        // 3
        val question3 = Question(
                3, "Zu welchem Land gehört diese Flagge?",
                R.drawable.ic_flag_of_brazil,
                "Belarus", "Belgien",
                "Thailand", "Brasilien", 4
        )

        questionList.add(question3)

        // 4
        val question4 = Question(
                4, "Zu welchem Land gehört diese Flagge?",
                R.drawable.ic_flag_of_belgium,
                "Bahamas", "Belgien",
                "Dom. Republik", "Niederlande", 2
        )

        questionList.add(question4)

        // 5
        val question5 = Question(
                5, "Zu welchem Land gehört diese Flagge?",
                R.drawable.ic_flag_of_fiji,
                "Indien", "Frankreich",
                "Fiji", "Neuseeland", 3
        )

        questionList.add(question5)

        // 6
        val question6 = Question(
                6, "Zu welchem Land gehört diese Flagge?",
                R.drawable.ic_flag_of_germany,
                "Deutschland", "Georgien",
                "Griechenland", "Keines der Länder", 1
        )

        questionList.add(question6)

        // 7
        val question7 = Question(
                7, "Zu welchem Land gehört diese Flagge?",
                R.drawable.ic_flag_of_denmark,
                "Deutschland", "Ägypten",
                "Dänemark", "Türkei", 3
        )

        questionList.add(question7)

        // 8
        val question8 = Question(
                8, "Zu welchem Land gehört diese Flagge?",
                R.drawable.ic_flag_of_india,
                "Irland", "Iran",
                "Ungarn", "Indien", 4
        )

        questionList.add(question1)

        // 9
        val question9 = Question(
                9, "Zu welchem Land gehört diese Flagge?",
                R.drawable.ic_flag_of_new_zealand,
                "Australien", "Neuseeland",
                "Tuvalu", "USA", 2
        )

        questionList.add(question9)

        // 10
        val question10 = Question(
                10, "Zu welchem Land gehört diese Flagge?",
                R.drawable.ic_flag_of_kuwait,
                "Kuwait", "Jordanien",
                "Sudan", "Israel", 1
        )

        questionList.add(question10)

        return questionList
    }
}