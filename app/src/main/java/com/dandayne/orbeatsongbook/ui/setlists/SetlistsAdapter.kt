package com.dandayne.orbeatsongbook.ui.setlists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.db.model.Setlist
import com.dandayne.orbeatsongbook.utils.RecyclerAdapterController
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class SetlistsAdapter(
    private val setlistController: SetlistController,
    private val adapterController: RecyclerAdapterController
) : RecyclerView.Adapter<SetlistsAdapter.SetlistHolder>(), KoinComponent {

    private val setlistsDataHolder: SetlistsDataHolder by inject()

    private val shownObserver = Observer<List<Setlist>> {
        adapterController.onDataSetChanged()
    }

    fun start() {
        setlistsDataHolder.shown.observeForever(shownObserver)
    }

    fun stop() {
        setlistsDataHolder.shown.removeObserver(shownObserver)
    }

    class SetlistHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.txt_name)
        val view = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetlistHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.setlists_item_view, parent, false)
        return SetlistHolder(view)
    }

    override fun getItemCount(): Int {
        return setlistsDataHolder.shown.value?.size ?: 0
    }

    @ObsoleteCoroutinesApi
    override fun onBindViewHolder(holder: SetlistHolder, position: Int) {
        val item = setlistsDataHolder.shown.value?.get(position)
        item?.apply {
            holder.name.text = setlistName
            holder.view.setOnClickListener {
                setlistsDataHolder.openSetlist(this.setlistId)
            }
            holder.view.setOnLongClickListener {
                setlistController.onSetlistItemHold(item)
                true
            }
        }
    }
}