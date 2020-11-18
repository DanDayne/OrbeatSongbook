package com.dandayne.orbeatsongbook.ui.files

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.db.model.File
import com.dandayne.orbeatsongbook.ui.pdf.PdfDataHolder
import com.dandayne.orbeatsongbook.utils.RecyclerAdapterController
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class FilesAdapter(
    private val filesController: FilesController,
    private val adapterController: RecyclerAdapterController
) : RecyclerView.Adapter<FilesAdapter.FileHolder>(), KoinComponent {

    private val filesDataHolder: FilesDataHolder by inject()
    private val pdfDataHolder: PdfDataHolder by inject()

    private val shownObserver = Observer<List<File>> {
        adapterController.onDataSetChanged()
    }

    fun start() {
        filesDataHolder.shown.observeForever(shownObserver)
    }

    fun stop() {
        filesDataHolder.shown.removeObserver(shownObserver)
    }

    class FileHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.txt_name)
        val view = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.files_item_view, parent, false)
        return FileHolder(view)
    }

    override fun getItemCount(): Int {
        return filesDataHolder.shown.value?.size ?: 0
    }

    @ObsoleteCoroutinesApi
    override fun onBindViewHolder(holder: FileHolder, position: Int) {
        val item = filesDataHolder.shown.value?.get(position)
        item?.apply {
            holder.name.text = fileName
            holder.view.setOnClickListener {
                filesDataHolder.shown.value?.let {
                    pdfDataHolder.openFile(it, it.indexOf(this))
                }
            }
            holder.view.setOnLongClickListener {
                filesController.onFileItemHold(item)
                true
            }
        }
    }
}