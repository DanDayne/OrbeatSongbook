package com.dandayne.orbeatsongbook.ui.setlists

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.databinding.FragmentSetlistsBinding
import com.dandayne.orbeatsongbook.db.model.File
import com.dandayne.orbeatsongbook.db.model.Setlist
import com.dandayne.orbeatsongbook.ui.files.FilesController
import com.dandayne.orbeatsongbook.ui.files.dialog.FileOptionsDialogFragment
import com.dandayne.orbeatsongbook.ui.setlists.dialog.AddMultipleFilesToSetlistDialogFragment
import com.dandayne.orbeatsongbook.ui.setlists.dialog.NewSetlistDialogFragment
import com.dandayne.orbeatsongbook.ui.setlists.dialog.SetlistOptionsDialogFragment
import com.dandayne.orbeatsongbook.utils.RecyclerAdapterController
import com.thesurix.gesturerecycler.GestureManager
import kotlinx.android.synthetic.main.fragment_setlists.view.*

class SetlistsFragment : Fragment(), SetlistController, FilesController,
    RecyclerAdapterController, DialogInterface.OnDismissListener {
    private val viewModel: SetlistsViewModel by viewModels()
    private lateinit var dataBinding: FragmentSetlistsBinding

    private lateinit var setlistsAdapter: SetlistsAdapter
    private lateinit var setlistContentAdapter: SetlistsContentAdapter

    override fun newSetlist() {
        NewSetlistDialogFragment().show(childFragmentManager, NewSetlistDialogFragment.TAG)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_setlists, container, true
        )
        dataBinding.viewModel = viewModel
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val setlistsRecycler: RecyclerView = view.findViewById(R.id.setlists_recycler_view)
        val setlistsLayoutManager = LinearLayoutManager(context)
        setlistsRecycler.layoutManager = setlistsLayoutManager
        setlistsAdapter = SetlistsAdapter(this, this)
        setlistsRecycler.adapter = setlistsAdapter
        setlistsRecycler.addItemDecoration(
            DividerItemDecoration(
                setlistsRecycler.context,
                DividerItemDecoration.VERTICAL
            )
        )

        val setlistContentRecycler: RecyclerView =
            view.findViewById(R.id.setlist_content_recycler_view)
        setlistContentRecycler.setHasFixedSize(false)
        val setlistContentLayoutManager = LinearLayoutManager(context)
        setlistContentRecycler.layoutManager = setlistContentLayoutManager
        setlistContentAdapter = SetlistsContentAdapter(this, this)
        setlistContentRecycler.adapter = setlistContentAdapter
        setlistContentRecycler.addItemDecoration(
            DividerItemDecoration(
                setlistsRecycler.context,
                DividerItemDecoration.VERTICAL
            )
        )
        GestureManager.Builder(setlistContentRecycler)
            .setManualDragEnabled(true)
            .setDragFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN)
            .build()

        setlistsAdapter.start()
        setlistContentAdapter.start()

        viewModel.showingProgressBar.observe(viewLifecycleOwner) {
            requireActivity().runOnUiThread {
                if (it == true) {
                    requireActivity().window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                    view.progress.visibility = View.VISIBLE
                } else {
                    view.progress.visibility = View.GONE
                    requireActivity().window.clearFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                }
            }
        }

        viewModel.showingSetlistContent.observe(viewLifecycleOwner) { setlist ->
            requireActivity().runOnUiThread {
                if (setlist != null) {
                    setlistContentRecycler.visibility = View.VISIBLE
                    view.add_button.setOnClickListener {
                        viewModel.refresh()
                        AddMultipleFilesToSetlistDialogFragment.newInstance(
                            setlist.setlist.setlistId,
                            setlist.setlist.setlistName
                        ).show(childFragmentManager, AddMultipleFilesToSetlistDialogFragment.TAG)
                    }
                    setlistsRecycler.visibility = View.GONE
                } else {
                    setlistContentRecycler.visibility = View.GONE

                    setlistsRecycler.visibility = View.VISIBLE
                    view.add_button.setOnClickListener {
                        newSetlist()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setlistContentAdapter.stop()
        setlistsAdapter.stop()
    }

    override fun onFileItemHold(file: File, setlist: Setlist?) {
        FileOptionsDialogFragment.newInstance(file.uri, setlist?.setlistId).show(
            childFragmentManager,
            FileOptionsDialogFragment.TAG
        )
    }

    override fun onSetlistItemHold(setlist: Setlist) {
        SetlistOptionsDialogFragment.newInstance(setlist.setlistId, setlist.setlistName).show(
            childFragmentManager,
            SetlistOptionsDialogFragment.TAG
        )
    }

    override fun onDataSetChanged() {
        activity?.runOnUiThread {
            setlistContentAdapter.notifyDataSetChanged()
            setlistsAdapter.notifyDataSetChanged()
        }
    }

    override fun onDismiss(p0: DialogInterface?) {
        viewModel.refresh()
    }
}