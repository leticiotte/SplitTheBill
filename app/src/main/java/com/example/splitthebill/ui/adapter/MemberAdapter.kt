package com.example.splitthebill.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.splitthebill.R
import com.example.splitthebill.domain.model.Item
import com.example.splitthebill.domain.model.Member
import com.example.splitthebill.domain.model.interfaces.MembersObserver
import com.example.splitthebill.domain.model.interfaces.OnItemClickListener
import com.example.splitthebill.ui.holder.MemberHolder

class MemberAdapter(private val context: Context, private var members: List<Member>, private val listener: OnItemClickListener): RecyclerView.Adapter<MemberHolder>() {

    private val observers = mutableListOf<MembersObserver>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberHolder {
        return MemberHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_member, parent, false), listener)
    }

    override fun onBindViewHolder(holder: MemberHolder, position: Int) {
        holder.name.text = members[position].name
        holder.amountPaid.text = context.getString(R.string.formatted_value, members[position].amountPaid)
        holder.image.setImageResource(R.drawable.user)
    }

    override fun getItemCount(): Int {
        return members.size
    }

    fun registerObserver(observer: MembersObserver) {
        observers.add(observer)
    }

    private fun notifyObservers() {
        for (observer in observers) {
            observer.onMembersChanged(members)
        }
    }

    fun setMembers(newItems: List<Member>) {
        if (members != newItems) {
            members = newItems
            notifyDataSetChanged()
            notifyObservers()
        }
    }
}
