package com.example.splitthebill.ui.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.splitthebill.R
import com.example.splitthebill.domain.model.interfaces.OnItemClickListener

class ItemHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener, View.OnLongClickListener{

    var image: ImageView
    var description: TextView
    var value: TextView
    private var listener: OnItemClickListener

    init {
        this.image = itemView.findViewById(R.id.viewItemIconImg)
        this.description = itemView.findViewById(R.id.viewItemDescriptionTv)
        this.value = itemView.findViewById(R.id.viewItemValueTv)
        this.listener = listener

        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    override fun onClick(p0: View?) {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            listener.onItemClick(position)
        }
    }

    override fun onLongClick(p0: View?): Boolean {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            listener.onItemLongPress(position)
            return true
        }
        return false
    }
}