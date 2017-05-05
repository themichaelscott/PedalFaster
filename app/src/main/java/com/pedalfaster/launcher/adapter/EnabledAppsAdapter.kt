package com.pedalfaster.launcher.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.pedalfaster.launcher.R
import com.pedalfaster.launcher.dagger.Injector
import com.pedalfaster.launcher.domain.pedalfasterapp.PedalfasterApp
import kotlinx.android.synthetic.main.item_pedalfaster_app.view.*

class EnabledAppsAdapter : RecyclerView.Adapter<EnabledAppsAdapter.ViewHolder>() {

    init {
        Injector.get().inject(this)
    }

    var itemClickListener: (PedalfasterApp, Boolean) -> Unit = { _, _ ->}

    var list: MutableList<PedalfasterApp> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent).apply {
            itemClickListener = { viewHolder, isSelected -> itemClickListener(list[viewHolder.adapterPosition], isSelected) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.appNameTextView.text = item.appName
        holder.appIconImageView.setImageDrawable(item.icon)
        holder.appSelectedCheckbox.isChecked = item.enabled
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pedalfaster_app, parent, false)) {

        val appNameTextView: TextView = itemView.appNameTextView
        val appIconImageView: ImageView = itemView.appIconImageView
        val appSelectedCheckbox: CheckBox = itemView.appSelectedCheckBox

        init {
            itemView.setOnClickListener {
                appSelectedCheckbox.isChecked = !appSelectedCheckbox.isChecked
                itemClickListener(this, appSelectedCheckbox.isChecked)
            }
        }

        var itemClickListener: (ViewHolder, Boolean) -> Unit = { _, _ ->}

    }

}
