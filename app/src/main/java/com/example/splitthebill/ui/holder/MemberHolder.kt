package com.example.splitthebill.ui.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.splitthebill.R
import com.example.splitthebill.domain.model.interfaces.OnItemClickListener

class MemberHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
    var image: ImageView
    var name: TextView
    var amountPaid: TextView
    private var listener: OnItemClickListener

    init {
        this.image = itemView.findViewById(R.id.viewMemberIconImg)
        this.name = itemView.findViewById(R.id.viewMemberNameTv)
        this.amountPaid = itemView.findViewById(R.id.viewMemberAmountPaidTv)
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