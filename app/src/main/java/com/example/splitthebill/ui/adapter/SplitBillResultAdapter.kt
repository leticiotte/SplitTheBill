package com.example.splitthebill.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.splitthebill.R
import com.example.splitthebill.domain.model.Member
import com.example.splitthebill.domain.model.SplitBillInfos
import com.example.splitthebill.domain.model.interfaces.MembersObserver
import com.example.splitthebill.domain.model.interfaces.OnItemClickListener
import com.example.splitthebill.ui.holder.MemberHolder
import com.example.splitthebill.ui.holder.SplitBillResultHolder

class SplitBillResultAdapter(private val context: Context, private var splitBillResultList: List<SplitBillInfos>): RecyclerView.Adapter<SplitBillResultHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SplitBillResultHolder {
        return SplitBillResultHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_split_bill_result, parent, false))
    }

    override fun onBindViewHolder(holder: SplitBillResultHolder, position: Int) {
        holder.name.text = splitBillResultList[position].name
        holder.value.text = context.getString(R.string.formatted_value, splitBillResultList[position].value)
        holder.image.setImageResource(R.drawable.user)
    }

    override fun getItemCount(): Int {
        return splitBillResultList.size
    }
}
