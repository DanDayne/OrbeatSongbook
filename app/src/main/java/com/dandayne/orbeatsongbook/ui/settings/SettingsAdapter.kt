package com.dandayne.orbeatsongbook.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dandayne.orbeatsongbook.databinding.ItemSettingsCategoryBinding
import com.dandayne.orbeatsongbook.databinding.ItemSettingsSwitchBinding
import com.dandayne.orbeatsongbook.db.DatabaseManager
import com.dandayne.orbeatsongbook.ui.settings.items.SettingsCategory
import com.dandayne.orbeatsongbook.ui.settings.items.SettingsItem
import com.dandayne.orbeatsongbook.ui.settings.items.SettingsSwitch
import com.dandayne.orbeatsongbook.utils.BaseViewHolder
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class SettingsAdapter(private val items: List<SettingsItem>) :
    RecyclerView.Adapter<BaseViewHolder<SettingsItem>>() {


    companion object {
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_SWITCH = 1
    }

    open class SettingsCategoryViewHolder(private val binding: ItemSettingsCategoryBinding) :
        BaseViewHolder<SettingsItem>(binding.root) {

        override fun bind(item: SettingsItem) {
            with(binding) {
                this.item = item as SettingsCategory
                executePendingBindings()
            }
        }
    }

    private inner class SettingsSwitchViewHolder(private val binding: ItemSettingsSwitchBinding) :
        BaseViewHolder<SettingsItem>(binding.root) {

        init {
            with(binding) {
                parent.setOnClickListener {
                    settingSwitch.isChecked = !settingSwitch.isChecked
                }
            }
        }

        override fun bind(item: SettingsItem) {
            with(binding) {
                settingSwitch.setOnCheckedChangeListener(null)
                this.item = item as SettingsSwitch
                executePendingBindings()
                settingSwitch.apply {
                    isChecked = item.getValueBlock()
                    switchStateDescription = if (isChecked) item.textOn else item.textOff
                    setOnCheckedChangeListener { _, checked ->
                        item.setValueBlock(checked)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is SettingsSwitch -> VIEW_TYPE_SWITCH
            else -> VIEW_TYPE_CATEGORY
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<SettingsItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_SWITCH -> SettingsSwitchViewHolder(
                ItemSettingsSwitchBinding.inflate(
                    layoutInflater, parent, false
                )
            )
            else -> SettingsCategoryViewHolder(
                ItemSettingsCategoryBinding.inflate(
                    layoutInflater, parent, false
                )
            )
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<SettingsItem>, position: Int) {
        holder.bind(items[position])
    }
}