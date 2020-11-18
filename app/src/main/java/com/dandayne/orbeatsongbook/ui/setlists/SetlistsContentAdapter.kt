package com.dandayne.orbeatsongbook.ui.setlists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.db.DatabaseManager
import com.dandayne.orbeatsongbook.db.model.SetlistWithFiles
import com.dandayne.orbeatsongbook.ui.files.FilesController
import com.dandayne.orbeatsongbook.ui.pdf.PdfDataHolder
import com.dandayne.orbeatsongbook.utils.RecyclerAdapterController
import com.thesurix.gesturerecycler.GestureAdapter
import com.thesurix.gesturerecycler.GestureViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class SetlistsContentAdapter(
    private val filesController: FilesController,
    private val adapterController: RecyclerAdapterController
) : GestureAdapter<OrderedFile, SetlistsContentAdapter.FileHolder>(), KoinComponent {

    private val setlistsDataHolder: SetlistsDataHolder by inject()
    private val pdfDataHolder: PdfDataHolder by inject()
    private val databaseManager: DatabaseManager by inject()


    private val shownObserver = Observer<SetlistWithFiles?> {
        GlobalScope.launch(Dispatchers.IO) {
            data.clear()
            it?.files?.forEach { file ->
                databaseManager.fetchFileSetlistCrossRef(
                    it.setlist.setlistId,
                    file.uri
                )?.order?.let { order ->
                    val orderedFile = OrderedFile(file, order)
                    data.add(orderedFile)
                }
            }
            data.sortBy { it.order }
            adapterController.onDataSetChanged()
        }
    }

    fun start() {
        setlistsDataHolder.showingSetlistContent.observeForever(shownObserver)
        setDataChangeListener(object : OnDataChangeListener<OrderedFile> {
            override fun onItemReorder(item: OrderedFile, fromPos: Int, toPos: Int) {
                setlistsDataHolder.toggleProgressBar(true)
                setlistsDataHolder.showingSetlistContent.value?.setlist?.setlistId?.let {
                    databaseManager.reorderSetlistByDragAndDrop(it, fromPos, toPos)
                        .invokeOnCompletion {
                            setlistsDataHolder.toggleProgressBar(false)
                            setlistsDataHolder.refresh()
                        }
                }
            }

            override fun onItemRemoved(item: OrderedFile, position: Int, direction: Int) {}
        })
    }


    fun stop() {
        setlistsDataHolder.showingSetlistContent.removeObserver(shownObserver)
    }

    class FileHolder(itemView: View) : GestureViewHolder<OrderedFile>(itemView) {
        var name: TextView = itemView.findViewById(R.id.txt_name)
        val view = itemView
        override val draggableView: ImageView = itemView.findViewById(R.id.order_handle)
        override fun canDrag() = true
        override fun canSwipe() = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.files_item_view, parent, false)
        return FileHolder(view)
    }

    @ObsoleteCoroutinesApi
    override fun onBindViewHolder(holder: FileHolder, position: Int) {
        val item = data[position]
        item.apply {
            holder.name.text = file.fileName
            holder.view.setOnClickListener {
                pdfDataHolder.openFile(data.map { it.file }, data.indexOf(this))
            }
            holder.view.setOnLongClickListener {
                filesController.onFileItemHold(
                    file,
                    setlistsDataHolder.showingSetlistContent.value?.setlist
                )
                true
            }
        }
        super.onBindViewHolder(holder, position)
    }
}