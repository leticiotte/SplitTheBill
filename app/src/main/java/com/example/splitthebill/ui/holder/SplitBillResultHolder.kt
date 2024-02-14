package com.example.splitthebill.ui.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.splitthebill.R
import com.example.splitthebill.domain.model.interfaces.OnItemClickListener

class SplitBillResultHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var image: ImageView
    var name: TextView
    var value: TextView

    init {
        this.image = itemView.findViewById(R.id.viewSplitBillResultIconImg)
        this.name = itemView.findViewById(R.id.viewSplitBillResultNameTv)
        this.value = itemView.findViewById(R.id.viewSplitBillResultValueTv)
    }
}