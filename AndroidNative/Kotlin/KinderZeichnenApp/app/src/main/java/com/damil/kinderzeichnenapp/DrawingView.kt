package com.damil.kinderzeichnenapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View



/**
 * Der Konstruktor für ViewForDrawing. Der Konstruktor ruft die setupDrawing() Methode auf.
 * Er wird nur aufgerufen, wenn das Layout das erste mal nach dem App-Start aufgebaut wird.
 *
 * @param context
 * @param attrs
 */

/**
 * Der Referenz-Link für die Erstellung der Klasse:
 * https://medium.com/@ssaurel/learn-to-create-a-paint-application-for-android-5b16968063f8
 */
//@SuppressLint("NewApi")
class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mDrawPath: CustomPath? =
            null // Variable von der inneren Klasse CustomPath.
    private var mCanvasBitmap: Bitmap? = null // Instanz einer Bitmap.

    private var mDrawPaint: Paint? = null // Paint Klasse enthält Informationen über Farben und Stärken, für das Zeichnen von Geometrien, Texten und Bitmaps.
    private var mCanvasPaint: Paint? = null // Instanz der Klasse Paint für die Leinwand.

    private var mBrushSize: Float = 0.toFloat() // Variable für die Strichstärke.

    // Variable, um die aktuelle Farbe des Striches zu speichern.
    private var color = Color.BLACK

    private val mUndoPaths = ArrayList<CustomPath>()

    /**
     * Variable für eine Leinwand, welche später initialisiert wird.
     *
     * Die Canvas [Leinwand] Klasse, enthält die Aufrufe zum Zeichnen. Um etwas zu zeichnen, werden
     * vier Grundkomponenten benötigt. Eine Bitmap, die die Pixel enthält, eine Leinwand, um die
     * Zeichenaufrufe nutzen zu können, ein Objekt der Klasse Paint und eine Form zum Zeichnen.
     */
    private var canvas: Canvas? = null

    // TODO(Schritt 1 : Erstellung und Initialisierung einer ArrayList, die die Pfade speichert)
    // START
    private val mPaths = ArrayList<CustomPath>() // ArrayList für Pfade
    // ENDE

    init {
        setUpDrawing()
    }

    /**
     * Die Methode initialisiert die Attribute der
     * ViewForDrawing Klasse.
     */
    private fun setUpDrawing() {
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)

        mDrawPaint!!.color = color

        mDrawPaint!!.style = Paint.Style.STROKE // Style eines Striches
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND // Der Anfang des Striches soll abgerundet sein
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND // Das Ende des Striches soll abgerundet sein

        mCanvasPaint = Paint(Paint.DITHER_FLAG) // Paint flag that enables dithering when blitting.

        //mBrushSize = 20.toFloat() // Anfangsgröße eines Striches festlegen.
    }

    override fun onSizeChanged(w: Int, h: Int, wprev: Int, hprev: Int) {
        super.onSizeChanged(w, h, wprev, hprev)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    /**
     * Methode wird aufgerufen, wenn Nutzer einen Strich auf der Leinwand malt.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /**
         * Bitmap mit der Position an der linken/ oberen Seite mit den spezifizierten Styles zeichnen und in
         * die aktuelle Matrix transformieren.
         *
         * Wenn die Bitmap und die Leinwand verschiedene Auflösungen haben, wird die Bitmap automatisch skaliert, sodass
         * diese an die Auflösung der Leinwand angepasst wird
         *
         * @param bitmap Bitmap, die gemalt werden soll
         * @param left Position auf der linken Seite der Bitmap
         * @param top Position an der oberen Seite der Bitmap
         * @param paint Style, mit dem etwas gezeichnet werden soll
         */
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)

        // TODO(Schritt 3 : Liste der Pfade/ Striche auf die Leinwand zeichnen.)
        //START

        for (p in mPaths) {
            mDrawPaint!!.strokeWidth = p.brushThickness
            mDrawPaint!!.color = p.color
            canvas.drawPath(p, mDrawPaint!!)
        }


        // ENDE


        if (!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }


    }

    /**
     * Methode arbeitet als Listener, der reagiert, wenn Nutzer anfängt zu zeichnen.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x // Touch-Event - X Koordinate
        val touchY = event.y // Touch-Event - Y Koordinate

        when (event.action) {
            // Wenn Nutzer anfängt Touch auszulösen
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset() // Linien und Kurven des Pfades löschen.
                mDrawPath!!.moveTo(
                        touchX,
                        touchY
                ) // Beginn der nächsten Kontur setzen (x,y).
            }

            // Wenn Nutzer gedrückt hält und den Finger bewegt
            MotionEvent.ACTION_MOVE -> {
                mDrawPath!!.lineTo(
                        touchX,
                        touchY
                ) // Linie vom letzten Punkt zu dem aktuellen Punkt zeichnen (x,y).
            }

            // Wenn Nutzer Touch-Event beendet
            MotionEvent.ACTION_UP -> {
                // TODO(Schritt 2 : Gezeichneten Pfad des Nutzers in die ArrayList hinzufügen.)
                // START
                mPaths.add(mDrawPath!!) // Wenn Touch-Event beendet wurde, wird in ArrayList gespeichert
                mDrawPath = CustomPath(color, mBrushSize)
                // ENDE
            }
            else -> return false
        }

        invalidate()
        return true
    }


    fun setSizeForBrush(newSize: Float){
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, resources.displayMetrics)
        mDrawPaint!!.strokeWidth = mBrushSize

    }

    fun setColor(newColor: String){

        color = Color.parseColor(newColor)
        mDrawPaint!!.color = color

    }

    fun onClickUndo(){
        if(mPaths.size > 0){
            mUndoPaths.add(mPaths.removeAt(mPaths.size - 1))
            invalidate()

        }
    }



    // Innere Klasse für unseren Pfad mit den Variablen Farben und der Strichstärke
    internal inner class CustomPath(var color: Int, var brushThickness: Float) : Path(){

    }
}