package com.dandayne.orbeatsongbook.ui.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.databinding.FragmentFilesBinding
import com.dandayne.orbeatsongbook.db.model.File
import com.dandayne.orbeatsongbook.db.model.Setlist
import com.dandayne.orbeatsongbook.ui.files.dialog.FileOptionsDialogFragment
import com.dandayne.orbeatsongbook.utils.RecyclerAdapterController

class FilesFragment : Fragment(), FilesController, RecyclerAdapterController {
    private val viewModel: FilesViewModel by viewModels()
    private lateinit var dataBinding: FragmentFilesBinding
    private lateinit var adapter: FilesAdapter

    override fun onFileItemHold(file: File, setlist: Setlist?) {
        FileOptionsDialogFragment.newInstance(file.uri, null)
            .show(childFragmentManager, FileOptionsDialogFragment.TAG)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_files, container, true
        )
        dataBinding.viewModel = viewModel

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler: RecyclerView = view.findViewById(R.id.files_recycler_view)
        val linearLayoutManager = LinearLayoutManager(context)
        recycler.layoutManager = linearLayoutManager
        adapter = FilesAdapter(this, this)
        recycler.adapter = adapter
        recycler.addItemDecoration(
            DividerItemDecoration(
                recycler.context,
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.start()
        viewModel.reloadFiles(view.findViewById(R.id.reload_files_button))
    }

    override fun onDestroyView() {
        adapter.stop()
        super.onDestroyView()
    }

    override fun onDataSetChanged() {
        activity?.runOnUiThread {
            adapter.notifyDataSetChanged()
        }
    }
}
