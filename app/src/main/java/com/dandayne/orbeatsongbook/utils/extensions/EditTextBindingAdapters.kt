package com.dandayne.orbeatsongbook.utils.extensions

import android.widget.EditText
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter

@BindingAdapter("errorId")
fun EditText.bindErrorId(@StringRes errorId: Int?) {
    error = errorId?.let { context.getString(it) }
}