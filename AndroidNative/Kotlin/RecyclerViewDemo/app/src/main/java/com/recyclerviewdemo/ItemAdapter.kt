package com.recyclerviewdemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_row.view.*

class ItemAdapter(val context: Context, val items: ArrayList<String>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     * Beeinflusst die Item-Ansicht, welche in XML erstellt wurde
     *
     * Erstellt ein neuen
     * {@link ViewHolder} und initialisiert einige private Felder, welche vom RecyclerView
     * genutzt werden.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.items_row,
                parent,
                false
            )
        )
    }

    /**
     * Verbindet jedes Element der ArrayList zu einem View-Element.
     *
     * Wird aufgerufen, wenn das RecyclerView einen neuen {@link ViewHolder} benötigt, um
     * ein Item anzuzeigen.
     *
     * Der neue ViewHolder sollte mit einem View erstellt werden, welches die Elemente des
     * gegebenen Typs anzeigen kann. Du kannst ein neues View manuell erstellen oder durch
     * eine XML-Layout-Datei beeinflussen.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)

        holder.tvItem.text = item

        // Hintergrundfarbe aktualisieren, basierend auf gerader oder ungerader Position in der Liste.
        if (position % 2 == 0) {
            holder.tvItem.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorLightGray
                )
            )
        } else {
            holder.tvItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        }
    }

    /**
     * Gibt die Anzahl der Elemente in der Liste zurück.
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Ein ViewHolder beschreibt ein Item und Metadaten, über die Position im RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Beinhaltet das TextView, in das alle Elemente eingefügt werden
        val tvItem = view.tvItem
    }
}