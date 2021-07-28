package com.damil.taschenrechner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // Wahrheitswerte (Boolean)
    var lastNumeric = false // Letzte Eingabe eine Zahl
    var lastDot = false // Punkt in der Eingabe enthalten

    override fun onCreate(savedInstanceState: Bundle?) {
        // Ruft den Super (Eltern) Konstruktor auf
        super.onCreate(savedInstanceState)
        // Verknüpft das XML-Layout mit der Kotlin Datei
        setContentView(R.layout.activity_main)

    }

    /*
    Hängt mit dem Befehl "append" den Text des angeklickten Buttons an das
    Textfeld an. Das übergebene View wird als Button gecasted. Der Button erbt
    vom View.
     */
    fun onDigit(view: View) {
        // Text des Buttons wird in Textfeld gespeichert
        tvInput.append((view as Button).text)
        // Letzte Eingabe war numerisch
        lastNumeric = true

    }

    /*
    Hängt, basierend auf dem gedrückten Button, +, -, *, / Operatoren an
    den Text des Textfeldes.
     */
    fun onOperator(view: View) {
        if (lastNumeric && !isOperatorAdded(tvInput.text.toString()))
            tvInput.append((view as Button).text)
        lastNumeric = false // Aktualisiert den Wert
        lastDot = false    // Zurücksetzen des Wertes
    }

    /*
    Prüft ob Berechnung mit ".0" endet. Wenn ja werden die letzten beiden Stellen
    entfernt.
     */
    private fun removeZeroAfterDot(result: String): String {
        var value = result
        // Prüft, ob String mit ".0" endet
        if (result.endsWith(".0")){
            // Erstellt einen Teilstring mit zwei Stellen weniger am Ende und
            // speichert diesen in value. Dadurch wird das ".0" entfernt.
            value = result.substring(0, result.length-2)
        }
        // value wird entweder unverändert oder ohne ".0" zurückgegeben.
        return value
    }

    /*
    Berechnet das Ergebnis der Eingabe
     */
    fun onEqual(view: View) {
        // Prüft, ob die letzte Zahl numerisch war.
        if (lastNumeric) {
            // Speichert die Nutzereingabe des Textfeldes in Variable tvValue
            var tvValue = tvInput.text.toString()
            var prefix = ""
            try {
                // Falls die Rechnung mit - startet, wird dieses in der Variable
                // Prefix gespeichert und das - wird entfernt
                if (tvValue.startsWith("-")){
                    prefix = "-"
                    // - wird entfernt, weil erste Stelle (Stelle 0) übersprungen wird
                    tvValue = tvValue.substring(1)
                }

                /*
                Prüft, welcher Operator in der Rechnung benutzt wurde. Je nach Operator
                werden die gleichen Schritte mit einem unterschiedlichen Operator durch-
                geführt.
                 */
                if (tvValue.contains("-")){
                    // Eingabe wird an der Stelle des - geteilt und in einem Array gespeichert
                    val splitValue = tvValue.split("-")
                    var one = splitValue[0] // Zahl 1 der Rechnung
                    var two = splitValue[1] // Zahl 2 der Rechnung

                    // Falls Zahl negativ war, wird das - vor die erste Zahl gehängt
                    if (!prefix.isEmpty()) {
                        one = prefix + one
                    }
                    /*
                    Die Variablen one und two sind Strings und werden in ein Double umgewandelt. Das
                    Ergebnis wird als String gespeichert und der Methode removeZeroAfterDot übergeben.
                    Die Rückgabe der Methode wird als Text des Textfeldes gesetzt.
                     */
                    tvInput.text = removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())

                } else if (tvValue.contains("+")){
                    // Eingabe wird an der Stelle des + geteilt und in einem Array gespeichert
                    val splitValue = tvValue.split("+")
                    var one = splitValue[0]
                    var two = splitValue[1]

                    // Falls Zahl negativ war, wird das - vor die erste Zahl gehängt
                    if (!prefix.isEmpty()) {
                        one = prefix + one
                    }
                    /*
                    Die Variablen one und two sind Strings und werden in ein Double umgewandelt. Das
                    Ergebnis wird als String gespeichert und der Methode removeZeroAfterDot übergeben.
                    Die Rückgabe der Methode wird als Text des Textfeldes gesetzt.
                    */
                    tvInput.text = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())

                } else if (tvValue.contains("/")){
                    // Eingabe wird an der Stelle des / geteilt und in einem Array gespeichert
                    val splitValue = tvValue.split("/")
                    var one = splitValue[0]
                    var two = splitValue[1]

                    // Falls Zahl negativ war, wird das - vor die erste Zahl gehängt
                    if (!prefix.isEmpty()) {
                        one = prefix + one
                    }
                    /*
                    Die Variablen one und two sind Strings und werden in ein Double umgewandelt. Das
                    Ergebnis wird als String gespeichert und der Methode removeZeroAfterDot übergeben.
                    Die Rückgabe der Methode wird als Text des Textfeldes gesetzt.
                    */
                    tvInput.text = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())

                } else if (tvValue.contains("*")){
                    // Eingabe wird an der Stelle des * geteilt und in einem Array gespeichert
                    val splitValue = tvValue.split("*")
                    var one = splitValue[0]
                    var two = splitValue[1]

                    // Falls Zahl negativ war, wird das - vor die erste Zahl gehängt
                    if (!prefix.isEmpty()) {
                        one = prefix + one
                    }
                    /*
                    Die Variablen one und two sind Strings und werden in ein Double umgewandelt. Das
                    Ergebnis wird als String gespeichert und der Methode removeZeroAfterDot übergeben.
                    Die Rückgabe der Methode wird als Text des Textfeldes gesetzt.
                     */
                    tvInput.text = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())
                }
                // Arithmetischer Fehler wird abgefangen und ausgegeben. Dadurch wird ein Crash der App verhindert.
            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    /*
    Der Text der Eingabe und die Prüfwerte für die numerische Zahl und den . werden zurückgesetzt
     */
    fun onClear(view: View) {
        tvInput.text = "" // Textfeld ist wieder leer
        lastDot = false // Wert wird auf Anfangswert zurückgesetzt
        lastNumeric = false // Wert wird auf Anfangswert zurückgesetzt
    }

    /*
    Hängt . an das Textfeld an
     */
    fun onDecimalPoint(view: View){
        // Wenn letzte Eingabe numerisch war, wird . angehängt
        if (lastNumeric && !lastDot) {
            tvInput.append((view as Button).text)
            lastNumeric = false // Aktualisiert den Wert
            lastDot = true // Aktualisiert den Wert
        }
    }

    /*
    Die Methode prüft, ob bereits ein Operator in der Rechnung benutzt wurde. Wenn ja wird true
    zurückgegeben, sonst false. Übergeben wird der Methode ein String mit der aktuellen Eingabe.
     */
    private fun isOperatorAdded(value: String): Boolean {
        // Falls an erster Stelle ein - enthalten ist, wird false zurückgegeben, weil dieses kein
        // Rechenoperator ist
        return if (value.startsWith("-")){
            false
        } else {
            // Falls einer der vier Rechenoperatoren vorhanden ist wird true zurückgegeben, sonst false
            value.contains("/") || value.contains("*") ||
                    value.contains("-") || value.contains("+")
        }
    }
}