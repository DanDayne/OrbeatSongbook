package com.dandayne.orbeatsongbook.ui.setlists

import com.dandayne.orbeatsongbook.db.model.Setlist

interface SetlistController {
    fun newSetlist()
    fun onSetlistItemHold(setlist: Setlist)
}