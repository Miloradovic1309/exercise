package com.damil.kinderzeichnenapp


import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.PictureInPictureParams
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_size.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var mImageButtonCurrentPaint: ImageButton? =
            null // Variable, in dem der aktuell ausgewählte ImageButton der Farben gespeichert wird


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        drawing_view.setSizeForBrush(20f)

        drawing_view.setSizeForBrush(20.toFloat()) // Standardwert für Strichstärke zu Anfang setzen.

        /**
         * Hier wählen wir die Anfangsfarbe aus. Dabei wollen wir schwarz als Anfangsfarbe setzen. Die Farbe ist
         * die zweite in der Farbpallete, also wählen wir die Position 1 im Array, weil dieses bei 0 anfängt zu zählen.
         * Außerdem setzen wir für den ImageButton den aktiven Hintergrund.
         */
        mImageButtonCurrentPaint = ll_paint_colors[1] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
        )

        ib_brush.setOnClickListener {
            this.showBrushSizeChooserDialog()
        }

        ib_gallery.setOnClickListener {
            if (isReadStorageAllowed()) {
                // Code für Zugriff auf Gallerie
                val pickphotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                startActivityForResult(pickphotoIntent, GALLERY)
            } else {
                requestStoragePermission()

            }
        }

        ib_undo.setOnClickListener {
            drawing_view.onClickUndo()
        }

        ib_save.setOnClickListener {
            if(isReadStorageAllowed()){
                BitmapCoroutine(getBitmapFromView(fl_drawing_view_container)).execute()
            } else{
                requestStoragePermission()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                try {
                    if (data!!.data != null) {
                        iv_background.visibility = View.VISIBLE
                        iv_background.setImageURI(data.data)
                    } else {
                        Toast.makeText(this, "Fehler beim Laden des Bildes", Toast.LENGTH_LONG).show()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }


    private fun showBrushSizeChooserDialog() {
        val brushDialog = Dialog(this);
        brushDialog.setContentView(R.layout.dialog_brush_size);
        brushDialog.setTitle("Strichsträke: ");

        val smallBtn = brushDialog.ib_small_brush;
        smallBtn.setOnClickListener {
            drawing_view.setSizeForBrush(10f)
            brushDialog.dismiss()
        }
        val mediumBtn: ImageButton = brushDialog.ib_medium_brush;
        mediumBtn.setOnClickListener {
            drawing_view.setSizeForBrush(20f)
            brushDialog.dismiss()
        };

        val largeBtn = brushDialog.ib_large_brush;
        largeBtn.setOnClickListener {
            drawing_view.setSizeForBrush(30f)
            brushDialog.dismiss()
        }
        brushDialog.show()
    }


    fun paintClicked(view: View) {
        if (view !== mImageButtonCurrentPaint) {
            // Aktualisierung der Farbe
            val imageButton = view as ImageButton
            // Tag wird benutzt, um die aktuelle Farbe und die zuvor ausgewählte Farbe zu tauscchen.
            // Tag speichert aktuelles View
            val colorTag = imageButton.tag.toString()
            // Unserer colorTag wird als Farbe gesetzt.
            drawing_view.setColor(colorTag)
            // Hintergrund des letzten aktiven und des aktiven Buttons ändern.
            imageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pallet_pressed))
            mImageButtonCurrentPaint!!.setImageDrawable(
                    ContextCompat.getDrawable(
                            this,
                            R.drawable.pallet_normal
                    )
            )

            // Die aktuelle View wird durch die übergebene View (ImageButton) aktualisiert.
            mImageButtonCurrentPaint = view
        }
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ).toString()
                )
        ) {
            Toast.makeText(this, "Die App braucht die Berechtigung, um einen Hintergrund hinzuzufügen", Toast.LENGTH_SHORT).show()
        }

        /**
         * Abfrage der Berechtigung. Diese muss im Manifest aufgeführt werden.
         */

        ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE)


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Die Berechtigung wurde erteilt, Hintergründe können hinzugefügt werden.", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Die Berechtigung wurde nicht erteilt, Hintergründe können nicht hinzugefügt werden.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isReadStorageAllowed(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED


    }


    private fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background

        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }

        view.draw(canvas)

        return returnedBitmap

    }

    companion object {
        private const val STORAGE_PERMISSION_CODE = 1
        private const val GALLERY = 2
    }

    private inner class BitmapCoroutine(val mBitmap: Bitmap?) : ViewModel() {

        private lateinit var mProgressDialog: Dialog

        fun execute() = viewModelScope.launch {
            onPreExecute()
            val result = doInBackground()
            onPostExecute(result)
        }

        private fun onPreExecute(){
            showProgressDialog()
        }

        private suspend fun doInBackground(vararg params: Any): String = withContext(Dispatchers.IO) {
            var result = ""

            if (mBitmap != null) {
                try {


                    val bytes = ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
                    val file = File(externalCacheDir!!.absoluteFile.toString() + File.separator + "KinderZeichnenApp_" + System.currentTimeMillis() / 1000 + ".png")

                    val fos = FileOutputStream(file)
                    fos.write(bytes.toByteArray())
                    fos.close()

                    result = file.absolutePath

                } catch (e: Exception){
                    result = ""
                    e.printStackTrace()
                }
            }

            return@withContext result
        }

        private fun onPostExecute(result: String){

            cancelProgressDialog()

            if(!result.isEmpty()){
                Toast.makeText(this@MainActivity, "Datei erfolgreich gespeichert. + $result", Toast.LENGTH_LONG).show()

                MediaScannerConnection.scanFile(
                    this@MainActivity, arrayOf(result), null
                ){
                        path, uri ->
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.putExtra(
                        Intent.EXTRA_STREAM,
                        uri
                    )
                    shareIntent.type = "image/jpeg"
                    startActivity(
                        Intent.createChooser(
                            shareIntent,
                            "Teilen"

                        )
                    )
                }
            } else{
                Toast.makeText(this@MainActivity, "Fehler beim Speichern.", Toast.LENGTH_LONG).show()
            }


        }



        private fun showProgressDialog(){
            mProgressDialog = Dialog(this@MainActivity)
            mProgressDialog.setContentView(R.layout.dialog_custom_progress)
            mProgressDialog.show()
        }

        private fun cancelProgressDialog(){
            mProgressDialog.dismiss()
        }
    }
}