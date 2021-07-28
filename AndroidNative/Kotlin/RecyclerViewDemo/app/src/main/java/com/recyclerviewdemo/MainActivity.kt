package com.recyclerviewdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    val LIST_VIEW = "LIST_VIEW" // Wert für ListView
    val GRID_VIEW = "GRID_VIEW" // Wert für GridView

    var currentVisibleView: String =
        LIST_VIEW // Variable wird dafür genutzt, um zu prüfen welches View sichtbar ist. Standardmäßig ist es die ListView.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        listView()

        fabSwitch.setOnClickListener { view ->

            if (currentVisibleView == LIST_VIEW) {
                fabSwitch.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_list
                    )
                )
                gridView()
            } else {
                fabSwitch.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_grid
                    )
                )
                listView()
            }
        }
    }

    /**
     * Funktion wird benutzt, um Elemente in einer Listen-Ansicht anzuzeigen
     */
    private fun listView() {

        currentVisibleView = LIST_VIEW // Aktuelles View wird aktualisiert

        // Setze den LayoutManager, den das RecyclerView benutzen wird.
        rvItemsList.layoutManager = LinearLayoutManager(this)
        // Adapter-Klasse (Hüllenklasse) wird initialisiert und die Liste
        // mit den Elementen als Parameter übergeben.
        val itemAdapter = ItemAdapter(this, getItemsList())
        // itemAdapter wird als Adapter-Instanz des RecyclerViews gesetzt,
        // um die Elemente zu beeinflussen.
        rvItemsList.adapter = itemAdapter
    }

    /**
     * Die Funktion wird benutzt, um die Elemente in einer Raster-Ansicht anzuzeigen
     */
    private fun gridView() {

        currentVisibleView = GRID_VIEW // Aktuelles View wird aktualisiert

        // Setze den LayoutManager, den das RecyclerView benutzen wird.
        rvItemsList.layoutManager = GridLayoutManager(this, 2)
        // Adapter-Klasse (Hüllenklasse) wird initialisiert und die Liste
        // mit den Elementen als Parameter übergeben.
        val itemAdapter = ItemAdapter(this, getItemsList())
        // itemAdapter wird als Adapter-Instanz des RecyclerViews gesetzt,
        // um die Elemente zu beeinflussen.
        rvItemsList.adapter = itemAdapter
    }

    /**
     * Die Funktion gibt die Elemente, welche in der Liste angezeigt werden sollen
     * als ArrayList zurück.
     */
    private fun getItemsList(): ArrayList<String> {
        val list = ArrayList<String>()

        list.add("Item Eins")
        list.add("Item Zwei")
        list.add("Item Drei")
        list.add("Item Vier")
        list.add("Item Fünf")
        list.add("Item Sechs")
        list.add("Item Sieben")
        list.add("Item Acht")
        list.add("Item Neun")
        list.add("Item Zehn")
        list.add("Item Elf")
        list.add("Item Zwölf")

        return list
    }
}